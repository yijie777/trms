package org.jeecg.modules.trms.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 镜像管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Data
@TableName("trms_docker_image")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="trms_docker_image对象", description="镜像管理")
public class TrmsDockerImage implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private Integer id;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**标签*/
	@Excel(name = "标签", width = 15)
    @ApiModelProperty(value = "标签")
    private String repotags;
	/**大小*/
	@Excel(name = "大小", width = 15)
    @ApiModelProperty(value = "大小")
    private Integer size;
	/**哈希值*/
	@Excel(name = "哈希值", width = 15)
    @ApiModelProperty(value = "哈希值")
    private String id64;
}
