package org.jeecg.modules.trms.config;

import lombok.Data;
import org.jeecg.modules.trms.config.vo.DockerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@Data
@ConfigurationProperties(prefix="trms")
public class TrmsBaseConfig {
    private DockerClient dockerClient;
}
