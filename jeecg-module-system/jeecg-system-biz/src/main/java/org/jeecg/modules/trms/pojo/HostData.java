package org.jeecg.modules.trms.pojo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* @Description: webssh数据传输
* @Author: NoCortY
* @Date: 2020/3/8
*/
@Data
public class HostData {
    //操作
    @ApiModelProperty(value = "START_HA CHECK_HA_MASTER CHECK_HA_SLAVE1 CHECK_HA_SLAVE2")
    private String operate;

    @ApiModelProperty(value = "SSH ip")
    private String host;
    @ApiModelProperty(value = "SSH 端口 默认22")
    private Integer port = 22;
    @ApiModelProperty(value = "SSH 用户名 默认root")
    private String username="root";
    @ApiModelProperty(value = "SSH 密码")
    private String password;
    @ApiModelProperty(value = "SSH 启动命令")
    private String command = "";

    @ApiModelProperty(value = "用户ID")
    private  String  userId;

}
