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
import org.jeecg.modules.trms.entity.TrmsDockerImage;
import org.jeecg.modules.trms.service.ITrmsDockerImageService;

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
 * @Description: 镜像管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Api(tags="A 镜像管理")
@RestController
@RequestMapping("/trms/trmsDockerImage")
@Slf4j
public class TrmsDockerImageController extends JeecgController<TrmsDockerImage, ITrmsDockerImageService> {
	@Autowired
	private ITrmsDockerImageService trmsDockerImageService;
	
	/**
	 * 分页列表查询
	 *
	 * @param trmsDockerImage
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "镜像管理-分页列表查询")
	@ApiOperation(value="镜像管理-分页列表查询", notes="镜像管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TrmsDockerImage>> queryPageList(TrmsDockerImage trmsDockerImage,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrmsDockerImage> queryWrapper = QueryGenerator.initQueryWrapper(trmsDockerImage, req.getParameterMap());
		Page<TrmsDockerImage> page = new Page<TrmsDockerImage>(pageNo, pageSize);
		IPage<TrmsDockerImage> pageList = trmsDockerImageService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param trmsDockerImage
	 * @return
	 */
	@AutoLog(value = "镜像管理-添加")
	@ApiOperation(value="镜像管理-添加", notes="镜像管理-添加")
	@RequiresPermissions("trms:trms_docker_image:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TrmsDockerImage trmsDockerImage) {
		trmsDockerImageService.save(trmsDockerImage);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trmsDockerImage
	 * @return
	 */
	@AutoLog(value = "镜像管理-编辑")
	@ApiOperation(value="镜像管理-编辑", notes="镜像管理-编辑")
	@RequiresPermissions("trms:trms_docker_image:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TrmsDockerImage trmsDockerImage) {
		trmsDockerImageService.updateById(trmsDockerImage);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "镜像管理-通过id删除")
	@ApiOperation(value="镜像管理-通过id删除", notes="镜像管理-通过id删除")
	@RequiresPermissions("trms:trms_docker_image:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		trmsDockerImageService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "镜像管理-批量删除")
	@ApiOperation(value="镜像管理-批量删除", notes="镜像管理-批量删除")
	@RequiresPermissions("trms:trms_docker_image:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trmsDockerImageService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "镜像管理-通过id查询")
	@ApiOperation(value="镜像管理-通过id查询", notes="镜像管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TrmsDockerImage> queryById(@RequestParam(name="id",required=true) String id) {
		TrmsDockerImage trmsDockerImage = trmsDockerImageService.getById(id);
		if(trmsDockerImage==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trmsDockerImage);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trmsDockerImage
    */
    @RequiresPermissions("trms:trms_docker_image:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrmsDockerImage trmsDockerImage) {
        return super.exportXls(request, trmsDockerImage, TrmsDockerImage.class, "镜像管理");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("trms:trms_docker_image:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, TrmsDockerImage.class);
    }

}
