package org.jeecg.modules.trms.util;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.jeecg.modules.trms.constant.ConstantPool;
import org.jeecg.modules.trms.pojo.HostData;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import static org.jeecg.modules.trms.constant.ConstantPool.KEYWORDS_HA_MASTER;
import static org.jeecg.modules.trms.constant.ConstantPool.KEYWORDS_HA_SLAVE2;

public class SSHUtil {
    public static void main(String[] args) throws JSchException, IOException {
//        SSHInfo sshInfo = new SSHInfo("192.168.160.100", 11022, "root", "123456");
//        System.out.println(automaticCorrection("HA_MASTER", sshInfo));
//        System.out.println(automaticCorrection("HA_SLAVE1", sshInfo));
//        System.out.println(automaticCorrection("HA_SLAVE2", sshInfo));
//      new SSHUtil().oneClickDeployment("START_HA", sshInfo);
    }

    public static void  oneClickDeployment(HostData sshInfo, SimpMessagingTemplate template) throws JSchException, IOException {
        // 创建JSch
        JSch jSch = new JSch();
        // 获取session
        Session session = jSch.getSession(sshInfo.getUsername(), sshInfo.getHost(), sshInfo.getPort());
        session.setPassword(sshInfo.getPassword());
        Properties config = new Properties();

        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        // 启动连接
        session.connect();
        ChannelExec exec = (ChannelExec) session.openChannel("exec");

        //选择操作
        if (sshInfo.getOperate().equals(ConstantPool.START_HA)) {
            exec.setCommand(startHA());
            exec.connect();
            exec.start();
            BufferedReader br = new BufferedReader(new InputStreamReader(exec.getInputStream()));
            String s;
            while ((s = br.readLine()) != null) {
                template.convertAndSend("/topic/"+sshInfo.getUserId(),s+"\n");
            }
            template.convertAndSend("/topic/"+sshInfo.getUserId(),"\r\n");

        }

        exec.disconnect();
        session.disconnect();
    }

    public static Boolean automaticCorrection(HostData sshInfo) throws JSchException, IOException {
        // 创建JSch
        JSch jSch = new JSch();
        // 获取session
        Session session = jSch.getSession(sshInfo.getUsername(), sshInfo.getHost(), sshInfo.getPort());
        session.setPassword(sshInfo.getPassword());
        Properties config = new Properties();

        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);
        // 启动连接
        session.connect();
        ChannelExec exec = (ChannelExec) session.openChannel("exec");

        //选择操作
//        exec.setCommand(startHA());

        boolean allKeywordsPresent = true;

        if (sshInfo.getOperate().equals(ConstantPool.CHECK_HA_MASTER)) {
            exec.setCommand("ssh master  \"source /etc/profile; jps\"\n");
            exec.connect();
            exec.start();
            String inStr = consumeInputStream(exec.getInputStream());
            for (String keyword : KEYWORDS_HA_MASTER) {
                if (!inStr.contains(keyword)) {
                    allKeywordsPresent = false;
                    break;
                }
            }
        } else if (sshInfo.getOperate().equals(ConstantPool.CHECK_HA_SLAVE1)) {
            exec.setCommand("ssh slave1  \"source /etc/profile; jps\"\n");
            exec.connect();
            exec.start();
            String inStr = consumeInputStream(exec.getInputStream());
            for (String keyword : KEYWORDS_HA_MASTER) {
                if (!inStr.contains(keyword)) {
                    allKeywordsPresent = false;
                    break;
                }
            }
        } else if (sshInfo.getOperate().equals(ConstantPool.CHECK_HA_SLAVE2)) {
            exec.setCommand("ssh slave2  \"source /etc/profile; jps\"\n");
            exec.connect();
            exec.start();
            String inStr = consumeInputStream(exec.getInputStream());
            for (String keyword : KEYWORDS_HA_SLAVE2) {
                if (!inStr.contains(keyword)) {
                    allKeywordsPresent = false;
                    break;
                }
            }
        }

        exec.disconnect();
        session.disconnect();
        exec.disconnect();
        session.disconnect();
        return allKeywordsPresent;

    }

    /**
     * 一键部署HA
     *
     * @return
     */
    public static String startHA() {
        StringBuilder sb = new StringBuilder();
        sb.append("ssh master  \"source /etc/profile; zkServer.sh start\"\n");
        sb.append("ssh slave1  \"source /etc/profile; zkServer.sh start\"\n");
        sb.append("ssh slave2  \"source /etc/profile; zkServer.sh start\"\n");
        sb.append("ssh master  \"source /etc/profile;  hdfs --daemon start journalnode\"\n");
        sb.append("ssh slave1  \"source /etc/profile;  hdfs --daemon start journalnode\"\n");
        sb.append("ssh slave2  \"source /etc/profile;  hdfs --daemon start journalnode\"\n");
        sb.append("ssh master  \"source /etc/profile;  start-dfs.sh\"\n");
        sb.append("ssh master  \"source /etc/profile;  start-yarn.sh\"\n");

        return sb.toString();
    }

    /**
     * 消费inputstream，并返回
     */
    public static String consumeInputStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            sb.append(s);
        }
        return sb.toString();
    }
}
