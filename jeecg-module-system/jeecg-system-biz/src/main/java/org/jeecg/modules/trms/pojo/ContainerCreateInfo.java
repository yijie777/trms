package org.jeecg.modules.trms.pojo;

import lombok.Data;

@Data
public class ContainerCreateInfo {
    private String id;
    private String imageName;
    private String tag;
    private String network;
}
