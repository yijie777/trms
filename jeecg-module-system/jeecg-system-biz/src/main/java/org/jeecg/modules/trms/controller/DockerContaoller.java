package org.jeecg.modules.trms.controller;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.ContainerNetwork;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.trms.entity.TrmsCourse;
import org.jeecg.modules.trms.entity.TrmsDockerContainer;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.service.ITrmsDockerContainerPortsService;
import org.jeecg.modules.trms.service.ITrmsDockerContainerService;
import org.jeecg.modules.trms.service.ITrmsDockerNetworkSettingsService;
import org.jeecg.modules.trms.util.DockerUtil;
import org.jeecg.modules.trms.vo.TrmsDockerContainerPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Api(tags="A Docker操作")
@Slf4j
@RestController
@RequestMapping("/trms/docker")
public class DockerContaoller {
    @Autowired
    private ITrmsDockerContainerService trmsDockerContainerService;
    @Autowired
    private ITrmsDockerNetworkSettingsService trmsDockerNetworkSettingsService;
    @Autowired
    private ITrmsDockerContainerPortsService trmsDockerContainerPortsService;

//    @AutoLog(value = "容器管理-添加")
//    @ApiOperation(value = "容器添加", notes = "课程导入时会建立容器")
//    @RequiresPermissions("trms:trms_docker_container:add")
    @RequestMapping(value = "/createMaster", method = RequestMethod.POST)
    public Result<String> createMaster(@RequestBody Object params) {
        DockerClient client = DockerUtil.client;
        List<CreateContainerResponse> containers = DockerUtil.createContainersHA(client, "1","100net","v3");
        for (CreateContainerResponse container : containers) {
            TrmsDockerContainer trmsDockerContainer = new TrmsDockerContainer();
            ArrayList<TrmsDockerContainerPorts> trmsDockerContainerPortsArrayList = new ArrayList<>();
            ArrayList<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsArrayList = new ArrayList<>();

            String id = container.getId();
            InspectContainerResponse exec = client.inspectContainerCmd(id).exec();
            Ports ports = exec.getNetworkSettings().getPorts();
            Map<ExposedPort, Ports.Binding[]> bindings = ports.getBindings();


            trmsDockerContainer.setName(exec.getName());
            trmsDockerContainer.setId64(exec.getId());
            trmsDockerContainer.setState(exec.getState().getStatus());
            trmsDockerContainer.setCommand(exec.getPath());
            trmsDockerContainer.setImageId64(exec.getImageId());
//            trmsDockerContainer.setImage(exec.);
            //            2023-09-06T15:10:06.479610547Z
//            trmsDockerContainer.setCreateTime();


            for (Map.Entry<ExposedPort, Ports.Binding[]> exposedPortEntry : bindings.entrySet()) {
                TrmsDockerContainerPorts port = new TrmsDockerContainerPorts();
                int PrivatePort = exposedPortEntry.getKey().getPort();
                List<Ports.Binding> collect = Arrays.stream(exposedPortEntry.getValue()).collect(Collectors.toList());
                System.out.println("内部端口：" + PrivatePort + "-----映射端口：" + collect.get(0).getHostPortSpec());
                port.setPrivatePort(PrivatePort);
                port.setPublicPort(Integer.getInteger(collect.get(0).getHostPortSpec()));
                trmsDockerContainerPortsArrayList.add(port);
            }
            for (Map.Entry<String, ContainerNetwork> stringContainerNetworkEntry : exec.getNetworkSettings().getNetworks().entrySet()) {
                TrmsDockerNetworkSettings network = new TrmsDockerNetworkSettings();
                network.setGateway(stringContainerNetworkEntry.getValue().getGateway());
                network.setNetworkName(stringContainerNetworkEntry.getKey());
                network.setIpAddress(stringContainerNetworkEntry.getValue().getIpAddress());
                network.setGateway(stringContainerNetworkEntry.getValue().getGateway());
                network.setIpPrefixLen(stringContainerNetworkEntry.getValue().getIpPrefixLen());
                network.setMacAddress(stringContainerNetworkEntry.getValue().getMacAddress());
                network.setId64(stringContainerNetworkEntry.getValue().getNetworkID());
                trmsDockerNetworkSettingsArrayList.add(network);
            }
            trmsDockerContainerService.saveMain(trmsDockerContainer, trmsDockerNetworkSettingsArrayList, trmsDockerContainerPortsArrayList);

        }

        return Result.OK("添加成功！");
    }
//    @ApiOperation(value="容器管理-分页列表查询", notes="容器管理-分页列表查询")
//    @GetMapping(value = "/list")
//    public Result<String> add(@RequestBody TrmsDockerContainerPage trmsDockerContainerPage) {
//        return Result.OK("添加成功！");
//    }
    @ApiOperation(value="容器-通过id查询", notes="容器-通过id查询 ")
    @GetMapping(value = "/queryById")
    public Result<InspectContainerResponse> queryById(@RequestParam(name="id",required=true) String id) {
        InspectContainerResponse containerById = DockerUtil.inspectContainer(id);
        return Result.OK(containerById);
    }


}
