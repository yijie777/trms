package org.jeecg.modules.trms.controller;

import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.vo.LoginUser;
import org.apache.shiro.SecurityUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import org.jeecg.modules.trms.entity.TrmsDockerContainer;
import org.jeecg.modules.trms.vo.TrmsDockerContainerPage;
import org.jeecg.modules.trms.service.ITrmsDockerContainerService;
import org.jeecg.modules.trms.service.ITrmsDockerNetworkSettingsService;
import org.jeecg.modules.trms.service.ITrmsDockerContainerPortsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.apache.shiro.authz.annotation.RequiresPermissions;

 /**
 * @Description: 容器管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Api(tags="容器管理")
@RestController
@RequestMapping("/trms/trmsDockerContainer")
@Slf4j
public class TrmsDockerContainerController {
	@Autowired
	private ITrmsDockerContainerService trmsDockerContainerService;
	@Autowired
	private ITrmsDockerNetworkSettingsService trmsDockerNetworkSettingsService;
	@Autowired
	private ITrmsDockerContainerPortsService trmsDockerContainerPortsService;
	
	/**
	 * 分页列表查询
	 *
	 * @param trmsDockerContainer
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "容器管理-分页列表查询")
	@ApiOperation(value="容器管理-分页列表查询", notes="容器管理-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<TrmsDockerContainer>> queryPageList(TrmsDockerContainer trmsDockerContainer,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<TrmsDockerContainer> queryWrapper = QueryGenerator.initQueryWrapper(trmsDockerContainer, req.getParameterMap());
		Page<TrmsDockerContainer> page = new Page<TrmsDockerContainer>(pageNo, pageSize);
		IPage<TrmsDockerContainer> pageList = trmsDockerContainerService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param trmsDockerContainerPage
	 * @return
	 */
	@AutoLog(value = "容器管理-添加")
	@ApiOperation(value="容器管理-添加", notes="容器管理-添加")
    @RequiresPermissions("trms:trms_docker_container:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody TrmsDockerContainerPage trmsDockerContainerPage) {
		TrmsDockerContainer trmsDockerContainer = new TrmsDockerContainer();
		BeanUtils.copyProperties(trmsDockerContainerPage, trmsDockerContainer);
		trmsDockerContainerService.saveMain(trmsDockerContainer, trmsDockerContainerPage.getTrmsDockerNetworkSettingsList(),trmsDockerContainerPage.getTrmsDockerContainerPortsList());
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param trmsDockerContainerPage
	 * @return
	 */
	@AutoLog(value = "容器管理-编辑")
	@ApiOperation(value="容器管理-编辑", notes="容器管理-编辑")
    @RequiresPermissions("trms:trms_docker_container:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody TrmsDockerContainerPage trmsDockerContainerPage) {
		TrmsDockerContainer trmsDockerContainer = new TrmsDockerContainer();
		BeanUtils.copyProperties(trmsDockerContainerPage, trmsDockerContainer);
		TrmsDockerContainer trmsDockerContainerEntity = trmsDockerContainerService.getById(trmsDockerContainer.getId());
		if(trmsDockerContainerEntity==null) {
			return Result.error("未找到对应数据");
		}
		trmsDockerContainerService.updateMain(trmsDockerContainer, trmsDockerContainerPage.getTrmsDockerNetworkSettingsList(),trmsDockerContainerPage.getTrmsDockerContainerPortsList());
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "容器管理-通过id删除")
	@ApiOperation(value="容器管理-通过id删除", notes="容器管理-通过id删除")
    @RequiresPermissions("trms:trms_docker_container:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		trmsDockerContainerService.delMain(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "容器管理-批量删除")
	@ApiOperation(value="容器管理-批量删除", notes="容器管理-批量删除")
    @RequiresPermissions("trms:trms_docker_container:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.trmsDockerContainerService.delBatchMain(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功！");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "容器管理-通过id查询")
	@ApiOperation(value="容器管理-通过id查询", notes="容器管理-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<TrmsDockerContainer> queryById(@RequestParam(name="id",required=true) String id) {
		TrmsDockerContainer trmsDockerContainer = trmsDockerContainerService.getById(id);
		if(trmsDockerContainer==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(trmsDockerContainer);

	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "网络信息-通过主表ID查询")
	@ApiOperation(value="网络信息-通过主表ID查询", notes="网络信息-通过主表ID查询")
	@GetMapping(value = "/queryTrmsDockerNetworkSettingsByMainId")
	public Result<IPage<TrmsDockerNetworkSettings>> queryTrmsDockerNetworkSettingsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList = trmsDockerNetworkSettingsService.selectByMainId(id);
		IPage <TrmsDockerNetworkSettings> page = new Page<>();
		page.setRecords(trmsDockerNetworkSettingsList);
		page.setTotal(trmsDockerNetworkSettingsList.size());
		return Result.OK(page);
	}
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "端口映射-通过主表ID查询")
	@ApiOperation(value="端口映射-通过主表ID查询", notes="端口映射-通过主表ID查询")
	@GetMapping(value = "/queryTrmsDockerContainerPortsByMainId")
	public Result<IPage<TrmsDockerContainerPorts>> queryTrmsDockerContainerPortsListByMainId(@RequestParam(name="id",required=true) String id) {
		List<TrmsDockerContainerPorts> trmsDockerContainerPortsList = trmsDockerContainerPortsService.selectByMainId(id);
		IPage <TrmsDockerContainerPorts> page = new Page<>();
		page.setRecords(trmsDockerContainerPortsList);
		page.setTotal(trmsDockerContainerPortsList.size());
		return Result.OK(page);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param trmsDockerContainer
    */
    @RequiresPermissions("trms:trms_docker_container:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, TrmsDockerContainer trmsDockerContainer) {
      // Step.1 组装查询条件查询数据
      QueryWrapper<TrmsDockerContainer> queryWrapper = QueryGenerator.initQueryWrapper(trmsDockerContainer, request.getParameterMap());
      LoginUser sysUser = (LoginUser) SecurityUtils.getSubject().getPrincipal();

     //配置选中数据查询条件
      String selections = request.getParameter("selections");
      if(oConvertUtils.isNotEmpty(selections)) {
           List<String> selectionList = Arrays.asList(selections.split(","));
           queryWrapper.in("id",selectionList);
      }
      //Step.2 获取导出数据
      List<TrmsDockerContainer>  trmsDockerContainerList = trmsDockerContainerService.list(queryWrapper);

      // Step.3 组装pageList
      List<TrmsDockerContainerPage> pageList = new ArrayList<TrmsDockerContainerPage>();
      for (TrmsDockerContainer main : trmsDockerContainerList) {
          TrmsDockerContainerPage vo = new TrmsDockerContainerPage();
          BeanUtils.copyProperties(main, vo);
          List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList = trmsDockerNetworkSettingsService.selectByMainId(main.getId().toString());
          vo.setTrmsDockerNetworkSettingsList(trmsDockerNetworkSettingsList);
          List<TrmsDockerContainerPorts> trmsDockerContainerPortsList = trmsDockerContainerPortsService.selectByMainId(main.getId().toString());
          vo.setTrmsDockerContainerPortsList(trmsDockerContainerPortsList);
          pageList.add(vo);
      }

      // Step.4 AutoPoi 导出Excel
      ModelAndView mv = new ModelAndView(new JeecgEntityExcelView());
      mv.addObject(NormalExcelConstants.FILE_NAME, "容器管理列表");
      mv.addObject(NormalExcelConstants.CLASS, TrmsDockerContainerPage.class);
      mv.addObject(NormalExcelConstants.PARAMS, new ExportParams("容器管理数据", "导出人:"+sysUser.getRealname(), "容器管理"));
      mv.addObject(NormalExcelConstants.DATA_LIST, pageList);
      return mv;
    }

    /**
    * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    @RequiresPermissions("trms:trms_docker_container:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
      MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
      Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
      for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
          // 获取上传文件对象
          MultipartFile file = entity.getValue();
          ImportParams params = new ImportParams();
          params.setTitleRows(2);
          params.setHeadRows(1);
          params.setNeedSave(true);
          try {
              List<TrmsDockerContainerPage> list = ExcelImportUtil.importExcel(file.getInputStream(), TrmsDockerContainerPage.class, params);
              for (TrmsDockerContainerPage page : list) {
                  TrmsDockerContainer po = new TrmsDockerContainer();
                  BeanUtils.copyProperties(page, po);
                  trmsDockerContainerService.saveMain(po, page.getTrmsDockerNetworkSettingsList(),page.getTrmsDockerContainerPortsList());
              }
              return Result.OK("文件导入成功！数据行数:" + list.size());
          } catch (Exception e) {
              log.error(e.getMessage(),e);
              return Result.error("文件导入失败:"+e.getMessage());
          } finally {
              try {
                  file.getInputStream().close();
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
      }
      return Result.OK("文件导入失败！");
    }

}
