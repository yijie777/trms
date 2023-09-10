package org.jeecg.modules.trms.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.trms.pojo.HostData;
import org.jeecg.modules.trms.service.WebSSHService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jcraft.jsch.JSchException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

import static org.jeecg.modules.trms.util.SSHUtil.oneClickDeployment;

@Controller
@AllArgsConstructor
@RequestMapping("/websocket1")
@Api(tags="A WebSocket")
public class WebSocketController {
    private SimpMessagingTemplate template;
    private WebSSHService webSSHService;

    @MessageMapping("/msg")
    public void sendMessage(@RequestBody HostData webSSHData) {
        webSSHService.recvHandle(webSSHData, template);  // 处理发送消息
    }

    @RequestMapping(value = "/test1",method = RequestMethod.POST)
    @ApiOperation(value="一键启动组件", notes="不是自动部署，只有配置完才可以一键启动，这个请求只能在已经建立WebSocket时使用")
    public Result<String> test(@RequestBody HostData hostData) throws JSchException, IOException {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        hostData.setUserId(user.getId());
        oneClickDeployment( hostData, template);
        return Result.ok("success");
    }

}
