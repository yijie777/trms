package org.jeecg.modules.trms.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 学生选课表
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Data
@TableName("trms_student_course")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="trms_student_course对象", description="学生选课表")
public class TrmsStudentCourse implements Serializable {
    private static final long serialVersionUID = 1L;

	/**主键*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "主键")
    private String id;
	/**学生ID*/
	@Excel(name = "学生ID", width = 15)
    @ApiModelProperty(value = "学生ID")
    private String studentId;
	/**课程ID*/
	@Excel(name = "课程ID", width = 15)
    @ApiModelProperty(value = "课程ID")
    private String courseId;
	/**学生选课的日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "学生选课的日期")
    private Date createTime;
	/**分数*/
	@Excel(name = "分数", width = 15)
    @ApiModelProperty(value = "分数")
    private Double score;
	/**容器列表*/
	@Excel(name = "容器列表", width = 15)
    @ApiModelProperty(value = "容器列表")
    private String containerList;


//    @ApiModelProperty(value = "课程信息")
    @TableField(exist = false)
    private TrmsCourse course;

//    @ApiModelProperty(value = "容器详细信息")
    @TableField(exist = false)
    private List<InspectContainerResponse> docker;
}
