package org.jeecg.modules.trms.vo;

import java.util.List;
import org.jeecg.modules.trms.entity.TrmsDockerContainer;
import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import lombok.Data;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelEntity;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Description: 容器管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Data
@ApiModel(value="trms_docker_containerPage对象", description="容器管理")
public class TrmsDockerContainerPage {

	/**id*/
	@ApiModelProperty(value = "id")
    private Integer id;
	/**容器名称*/
	@Excel(name = "容器名称", width = 15)
	@ApiModelProperty(value = "容器名称")
    private String name;
	/**容器状态*/
	@Excel(name = "容器状态", width = 15)
	@ApiModelProperty(value = "容器状态")
    private String state;
	/**镜像哈希*/
	@Excel(name = "镜像哈希", width = 15)
	@ApiModelProperty(value = "镜像哈希")
    private String imageId64;
	/**镜像名称*/
	@Excel(name = "镜像名称", width = 15)
	@ApiModelProperty(value = "镜像名称")
    private String image;
	/**启动命令*/
	@Excel(name = "启动命令", width = 15)
	@ApiModelProperty(value = "启动命令")
    private String command;
	/**哈希值*/
	@Excel(name = "哈希值", width = 15)
	@ApiModelProperty(value = "哈希值")
    private String id64;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
    private Date createTime;
	
	@ExcelCollection(name="网络信息")
	@ApiModelProperty(value = "网络信息")
	private List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList;
	@ExcelCollection(name="端口映射")
	@ApiModelProperty(value = "端口映射")
	private List<TrmsDockerContainerPorts> trmsDockerContainerPortsList;
	
}
