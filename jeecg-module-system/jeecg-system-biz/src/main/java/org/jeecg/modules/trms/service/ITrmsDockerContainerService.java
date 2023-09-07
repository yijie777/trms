package org.jeecg.modules.trms.service;

import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import org.jeecg.modules.trms.entity.TrmsDockerContainer;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 容器管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface ITrmsDockerContainerService extends IService<TrmsDockerContainer> {

	/**
	 * 添加一对多
	 *
	 * @param trmsDockerContainer
	 * @param trmsDockerNetworkSettingsList
	 * @param trmsDockerContainerPortsList
	 */
	public void saveMain(TrmsDockerContainer trmsDockerContainer,List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList,List<TrmsDockerContainerPorts> trmsDockerContainerPortsList) ;
	
	/**
	 * 修改一对多
	 *
	 * @param trmsDockerContainer
	 * @param trmsDockerNetworkSettingsList
	 * @param trmsDockerContainerPortsList
	 */
	public void updateMain(TrmsDockerContainer trmsDockerContainer,List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList,List<TrmsDockerContainerPorts> trmsDockerContainerPortsList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
