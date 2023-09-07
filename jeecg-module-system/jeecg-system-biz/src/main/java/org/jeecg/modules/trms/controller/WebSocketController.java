package org.jeecg.modules.trms.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.trms.constant.ConstantPool;
import org.jeecg.modules.trms.pojo.HostData;
import org.jeecg.modules.trms.service.WebSSHService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jcraft.jsch.JSchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@AllArgsConstructor
@RequestMapping("/websocket1")
public class WebSocketController{
    private SimpMessagingTemplate template;
    private WebSSHService webSSHService;
    @MessageMapping("/msg")
    public void sendMessage(@RequestBody HostData webSSHData) {
        webSSHService.recvHandle(webSSHData, template);  // 处理发送消息
    }
}
