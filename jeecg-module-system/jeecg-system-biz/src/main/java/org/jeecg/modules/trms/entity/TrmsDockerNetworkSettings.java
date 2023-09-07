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
 * @Description: 网络信息
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@ApiModel(value="trms_docker_network_settings对象", description="网络信息")
@Data
@TableName("trms_docker_network_settings")
public class TrmsDockerNetworkSettings implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**容器ID*/
    @ApiModelProperty(value = "容器ID")
    private Integer containerId;
	/**网络ID*/
	@Excel(name = "网络ID", width = 15)
    @ApiModelProperty(value = "网络ID")
    private Integer networkId;
	/**网络名称*/
	@Excel(name = "网络名称", width = 15)
    @ApiModelProperty(value = "网络名称")
    private String networkName;
	/**mac地址*/
	@Excel(name = "mac地址", width = 15)
    @ApiModelProperty(value = "mac地址")
    private String macAddress;
	/**网关*/
	@Excel(name = "网关", width = 15)
    @ApiModelProperty(value = "网关")
    private String gateway;
	/**IP地址*/
	@Excel(name = "IP地址", width = 15)
    @ApiModelProperty(value = "IP地址")
    private String ipAddress;
	/**前缀长度*/
	@Excel(name = "前缀长度", width = 15)
    @ApiModelProperty(value = "前缀长度")
    private Integer ipPrefixLen;
	/**哈希值*/
	@Excel(name = "哈希值", width = 15)
    @ApiModelProperty(value = "哈希值")
    private String id64;
}
