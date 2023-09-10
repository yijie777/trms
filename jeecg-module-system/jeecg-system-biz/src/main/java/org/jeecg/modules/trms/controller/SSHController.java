package org.jeecg.modules.trms.controller;

import com.jcraft.jsch.JSchException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.trms.pojo.HostData;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.jeecg.modules.trms.util.SSHUtil.automaticCorrection;

@Api(tags = "A SSH操作")
@Slf4j
@RestController
@RequestMapping("/trms/ssh")
public class SSHController {
    @PostMapping("/test2")
    @ApiOperation(value = "JPS命令", notes = "CHECK_HA_MASTER")
    public Result<String> test2(@RequestBody HostData hostData) throws JSchException, IOException {
        LoginUser user = (LoginUser) SecurityUtils.getSubject().getPrincipal();
        hostData.setUserId(user.getId());
        Boolean aBoolean = automaticCorrection(hostData);
        return aBoolean ? Result.OK("启动成功") : Result.error("启动失败");
    }
}
