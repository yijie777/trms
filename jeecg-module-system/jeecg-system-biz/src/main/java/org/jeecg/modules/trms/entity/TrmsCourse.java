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
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Data
@TableName("trms_course")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="trms_course对象", description="课程表")
public class TrmsCourse implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**课程名称*/
	@Excel(name = "课程名称", width = 15)
    @ApiModelProperty(value = "课程名称")
    private String courseName;
	/**授课教师*/
	@Excel(name = "授课教师", width = 15)
    @ApiModelProperty(value = "授课教师")
    private String tearcherId;
	/**课程描述*/
	@Excel(name = "课程描述", width = 15)
    @ApiModelProperty(value = "课程描述")
    private String description;
	/**学分*/
	@Excel(name = "学分", width = 15)
    @ApiModelProperty(value = "学分")
    private Double credits;
	/**资源*/
	@Excel(name = "资源", width = 15)
    @ApiModelProperty(value = "资源")
    private String resourceUrl;
	/**封面*/
	@Excel(name = "封面", width = 15)
    @ApiModelProperty(value = "封面")
    private String imageUrl;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建日期")
    private Date createTime;
}
