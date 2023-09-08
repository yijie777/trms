package org.jeecg.modules.trms.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.trms.entity.TrmsCourse;
import org.jeecg.modules.trms.service.ITrmsCourseService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

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
 * @Description: 课程表
 * @Author: jeecg-boot
 * @Date:   2023-09-08
 * @Version: V1.0
 */
@Api(tags="课程表")
@RestController
@RequestMapping("/trms/trmsCourse")
@Slf4j
public class TrmsCourseController extends JeecgController<TrmsCourse, ITrmsCourseService> {
	@Autowired
	private ITrmsCourseService trmsCourseService;
	
	/**
	 * 分页列表查询
	 *
	 * @param trmsCourse
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "课程表-分页列表查询")
	@ApiOperation(value="课程表-分页列表查询", notes="课程表-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TrmsCourse>> queryPageList(TrmsCourse trmsCourse,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrmsCourse> queryWrapper = QueryGenerator.initQueryWrapper(trmsCourse, req.getParameterMap());
		Page<TrmsCourse> page = new Page<TrmsCourse>(pageNo, pageSize);
		IPage<TrmsCourse> pageList = trmsCourseService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param trmsCourse
	 * @return
	 */
	@AutoLog(value = "课程表-添加")
	@ApiOperation(value="课程表-添加", notes="课程表-添加")
	@RequiresPermissions("trms:trms_course:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TrmsCourse trmsCourse) {
		trmsCourseService.save(trmsCourse);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trmsCourse
	 * @return
	 */
	@AutoLog(value = "课程表-编辑")
	@ApiOperation(value="课程表-编辑", notes="课程表-编辑")
	@RequiresPermissions("trms:trms_course:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TrmsCourse trmsCourse) {
		trmsCourseService.updateById(trmsCourse);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "课程表-通过id删除")
	@ApiOperation(value="课程表-通过id删除", notes="课程表-通过id删除")
	@RequiresPermissions("trms:trms_course:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		trmsCourseService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "课程表-批量删除")
	@ApiOperation(value="课程表-批量删除", notes="课程表-批量删除")
	@RequiresPermissions("trms:trms_course:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trmsCourseService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "课程表-通过id查询")
	@ApiOperation(value="课程表-通过id查询", notes="课程表-通过id查询 ")
	@GetMapping(value = "/queryById")
	public Result<TrmsCourse> queryById(@RequestParam(name="id",required=true) String id) {
		TrmsCourse trmsCourse = trmsCourseService.getById(id);
		if(trmsCourse==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trmsCourse);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trmsCourse
    */
    @RequiresPermissions("trms:trms_course:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrmsCourse trmsCourse) {
        return super.exportXls(request, trmsCourse, TrmsCourse.class, "课程表");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("trms:trms_course:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TrmsCourse.class);
    }

}
