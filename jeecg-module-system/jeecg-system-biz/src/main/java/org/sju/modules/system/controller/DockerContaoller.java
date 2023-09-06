package org.sju.modules.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.sju.modules.system.util.DockerUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/sys/docker")
public class DockerContaoller {
    @RequestMapping(value = "/createMaster", method = RequestMethod.GET)
    public  Object queryPageList() {
        return null;
    }

}
