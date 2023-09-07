package org.jeecg.modules.trms.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.trms.constant.ConstantPool;
import org.jeecg.modules.trms.pojo.HostData;
import org.jeecg.modules.trms.pojo.SSHConnectInfo;
import org.jeecg.modules.trms.service.WebSSHService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: WebSSH业务逻辑实现
 * @Author: NoCortY
 * @Date: 2020/3/8
 */
@Slf4j
@AllArgsConstructor
@Service
public class WebSSHServiceImpl implements WebSSHService {
    //存放ssh连接信息的map
    private static Map<Integer, Object> sshMap = new ConcurrentHashMap<>();

    // 初始化 ssh 连接
    @Override
    public void initConnection(Integer userId) {
        JSch jSch = new JSch();
        SSHConnectInfo sshConnectInfo = new SSHConnectInfo();
        sshConnectInfo.setjSch(jSch);
        //将这个ssh连接信息放入map中
        sshMap.put(userId, sshConnectInfo);
    }
    @Override
    public void recvHandle(HostData webSSHData, SimpMessagingTemplate template) {
        // 连接 ssh：connect 指令
        if (webSSHData!=null && ConstantPool.WEBSSH_OPERATE_CONNECT.equals(webSSHData.getOperate())) {
            //找到刚才存储的ssh连接对象
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(1024);
            try {
                connectToSSH(sshConnectInfo, webSSHData, template);
            } catch (JSchException | IOException e) {
                log.error("webssh连接异常");
                log.error("异常信息:{}", e.getMessage());
            }
        }
        // 输入命令（把命令输到后台终端）command 指令
        else if (webSSHData!=null && ConstantPool.WEBSSH_OPERATE_COMMAND.equals(webSSHData.getOperate())) {
            SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(1024);
            if (sshConnectInfo != null) {
                try {
                    transToSSH(sshConnectInfo.getChannel(), webSSHData.getCommand());
                } catch (IOException e) {
                    log.error("webssh连接异常");
                    log.error("异常信息:{}", e.getMessage());
                }
            }
        } else {
            log.error("不支持的操作");
        }
    }

    // 使用jsch连接终端
    private void connectToSSH(SSHConnectInfo sshConnectInfo, HostData webSSHData, SimpMessagingTemplate template) throws JSchException, IOException {
        //获取jsch的会话
        Session session = sshConnectInfo.getjSch().getSession(webSSHData.getUsername(), webSSHData.getHost(), webSSHData.getPort());
        Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        //设置密码
        session.setPassword(webSSHData.getPassword());
        //连接  超时时间30s
        session.connect(30000);
        //开启shell通道
        Channel channel = session.openChannel("shell");
        //通道连接 超时时间3s
        channel.connect(3000);

        //设置channel
        sshConnectInfo.setChannel(channel);
        //转发消息给终端
        transToSSH(channel, "\r");

        //读取终端返回的信息流
        InputStream inputStream = channel.getInputStream();
        try {
            //循环读取
            byte[] buffer = new byte[1024];
            int i = 0;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                template.convertAndSend("/topic/" + webSSHData.getUserId(), new String(Arrays.copyOfRange(buffer, 0, i)));
            }
        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channel.disconnect();
            if (inputStream != null) {
                inputStream.close();
            }
        }

    }

    private void transToSSH(Channel channel, String command) throws IOException {
        if (channel != null) {
            OutputStream outputStream = channel.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }
    @Override
    public void sendMessage(WebSocketSession session, byte[] buffer) throws IOException {
        session.sendMessage(new TextMessage(buffer));
    }

    // 关闭连接
    @Override
    public void close(Integer userId) {
        SSHConnectInfo sshConnectInfo = (SSHConnectInfo) sshMap.get(userId);
        if (sshConnectInfo != null) {
            //断开连接
            if (sshConnectInfo.getChannel() != null) {
                sshConnectInfo.getChannel().disconnect();
            }
            //map中移除
            sshMap.remove(userId);
        }
    }


}
