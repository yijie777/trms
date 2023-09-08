package org.jeecg.modules.trms.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.trms.entity.TrmsCourse;
import org.jeecg.modules.trms.entity.TrmsStudentCourse;
import org.jeecg.modules.trms.service.ITrmsCourseService;
import org.jeecg.modules.trms.service.ITrmsStudentCourseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecg.modules.trms.util.DockerUtil;
import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @Description: 学生选课表
 * @Author: jeecg-boot
 * @Date: 2023-09-08
 * @Version: V1.0
 */
@Api(tags = "A 学生选课表")
@RestController
@RequestMapping("/trms/trmsStudentCourse")
@Slf4j
public class TrmsStudentCourseController extends JeecgController<TrmsStudentCourse, ITrmsStudentCourseService> {
    @Autowired
    private ITrmsStudentCourseService trmsStudentCourseService;
    @Autowired
    private ITrmsCourseService trmsCourseService;

    /**
     * 分页列表查询
     *
     * @param trmsStudentCourse
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "学生选课表-分页列表查询")
    @ApiOperation(value = "学生选课表-分页列表查询", notes = "学生选课表-分页列表查询，这个是根据当前登录用户的学号查询自己选的课程 展示为卡片列表")
    @GetMapping(value = "/list")
    public Result<IPage<TrmsStudentCourse>> queryPageList(TrmsStudentCourse trmsStudentCourse,
                                                          @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                          @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                          HttpServletRequest req) {
        QueryWrapper<TrmsStudentCourse> queryWrapper = QueryGenerator.initQueryWrapper(trmsStudentCourse, req.getParameterMap());
//		 MPJLambdaWrapper<TrmsStudentCourse> wrapper = new MPJLambdaWrapper<TrmsStudentCourse>()
//				 .selectAll(TrmsStudentCourse.class)
//				 .selectAll(TrmsCourse.class)
//				 .leftJoin(TrmsCourse.class,TrmsCourse::getId,TrmsStudentCourse::getCourseId)

        Page<TrmsStudentCourse> page = new Page<TrmsStudentCourse>(pageNo, pageSize);

        IPage<TrmsStudentCourse> pageList = trmsStudentCourseService.page(page, queryWrapper);
        for (TrmsStudentCourse record : pageList.getRecords()) {
            record.setCourse(trmsCourseService.getById(record.getCourseId()));
            List<InspectContainerResponse> docker = new ArrayList<>();
            for (String id : record.getContainerList().split(",")) {
                docker.add(DockerUtil.inspectContainer(id));
            }
            record.setDocker(docker);
        }
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param trmsStudentCourse
     * @return
     */
    @AutoLog(value = "学生选课表-添加")
    @ApiOperation(value = "学生选课表-添加", notes = "学生选课表-添加")
    @RequiresPermissions("trms:trms_student_course:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody TrmsStudentCourse trmsStudentCourse) {
        trmsStudentCourseService.save(trmsStudentCourse);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param trmsStudentCourse
     * @return
     */
    @AutoLog(value = "学生选课表-编辑")
    @ApiOperation(value = "学生选课表-编辑", notes = "学生选课表-编辑")
    @RequiresPermissions("trms:trms_student_course:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody TrmsStudentCourse trmsStudentCourse) {
        trmsStudentCourseService.updateById(trmsStudentCourse);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "学生选课表-通过id删除")
    @ApiOperation(value = "学生选课表-通过id删除", notes = "学生选课表-通过id删除")
    @RequiresPermissions("trms:trms_student_course:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        trmsStudentCourseService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "学生选课表-批量删除")
    @ApiOperation(value = "学生选课表-批量删除", notes = "学生选课表-批量删除")
    @RequiresPermissions("trms:trms_student_course:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.trmsStudentCourseService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "学生选课表-通过id查询")
    @ApiOperation(value = "学生选课表-通过id查询", notes = "学生选课表-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<TrmsStudentCourse> queryById(@RequestParam(name = "id", required = true) String id) {
        TrmsStudentCourse trmsStudentCourse = trmsStudentCourseService.getById(id);
        if (trmsStudentCourse == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(trmsStudentCourse);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param trmsStudentCourse
     */
    @RequiresPermissions("trms:trms_student_course:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrmsStudentCourse trmsStudentCourse) {
        return super.exportXls(request, trmsStudentCourse, TrmsStudentCourse.class, "学生选课表");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("trms:trms_student_course:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TrmsStudentCourse.class);
    }

}
