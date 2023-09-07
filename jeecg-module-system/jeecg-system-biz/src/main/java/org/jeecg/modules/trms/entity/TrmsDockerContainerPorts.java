package org.jeecg.modules.trms.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 端口映射
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@ApiModel(value="trms_docker_container_ports对象", description="端口映射")
@Data
@TableName("trms_docker_container_ports")
public class TrmsDockerContainerPorts implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**容器ID*/
    @ApiModelProperty(value = "容器ID")
    private Integer containerId;
	/**IP地址*/
	@Excel(name = "IP地址", width = 15)
    @ApiModelProperty(value = "IP地址")
    private String ip;
	/**内部端口*/
	@Excel(name = "内部端口", width = 15)
    @ApiModelProperty(value = "内部端口")
    private Integer privatePort;
	/**外部端口*/
	@Excel(name = "外部端口", width = 15)
    @ApiModelProperty(value = "外部端口")
    private Integer publicPort;
	/**通信类型*/
	@Excel(name = "通信类型", width = 15)
    @ApiModelProperty(value = "通信类型")
    private String type;
}
