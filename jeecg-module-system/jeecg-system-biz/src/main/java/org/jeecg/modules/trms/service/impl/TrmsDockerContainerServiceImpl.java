package org.jeecg.modules.trms.service.impl;

import org.jeecg.modules.trms.entity.TrmsDockerContainer;
import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import org.jeecg.modules.trms.mapper.TrmsDockerNetworkSettingsMapper;
import org.jeecg.modules.trms.mapper.TrmsDockerContainerPortsMapper;
import org.jeecg.modules.trms.mapper.TrmsDockerContainerMapper;
import org.jeecg.modules.trms.service.ITrmsDockerContainerService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 容器管理
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Service
public class TrmsDockerContainerServiceImpl extends ServiceImpl<TrmsDockerContainerMapper, TrmsDockerContainer> implements ITrmsDockerContainerService {

	@Autowired
	private TrmsDockerContainerMapper trmsDockerContainerMapper;
	@Autowired
	private TrmsDockerNetworkSettingsMapper trmsDockerNetworkSettingsMapper;
	@Autowired
	private TrmsDockerContainerPortsMapper trmsDockerContainerPortsMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(TrmsDockerContainer trmsDockerContainer, List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList,List<TrmsDockerContainerPorts> trmsDockerContainerPortsList) {
		trmsDockerContainerMapper.insert(trmsDockerContainer);
		if(trmsDockerNetworkSettingsList!=null && trmsDockerNetworkSettingsList.size()>0) {
			for(TrmsDockerNetworkSettings entity:trmsDockerNetworkSettingsList) {
				//外键设置
				entity.setContainerId(trmsDockerContainer.getId());
				trmsDockerNetworkSettingsMapper.insert(entity);
			}
		}
		if(trmsDockerContainerPortsList!=null && trmsDockerContainerPortsList.size()>0) {
			for(TrmsDockerContainerPorts entity:trmsDockerContainerPortsList) {
				//外键设置
				entity.setContainerId(trmsDockerContainer.getId());
				trmsDockerContainerPortsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(TrmsDockerContainer trmsDockerContainer,List<TrmsDockerNetworkSettings> trmsDockerNetworkSettingsList,List<TrmsDockerContainerPorts> trmsDockerContainerPortsList) {
		trmsDockerContainerMapper.updateById(trmsDockerContainer);
		
		//1.先删除子表数据
		trmsDockerNetworkSettingsMapper.deleteByMainId(trmsDockerContainer.getId().toString());
		trmsDockerContainerPortsMapper.deleteByMainId(trmsDockerContainer.getId().toString());
		
		//2.子表数据重新插入
		if(trmsDockerNetworkSettingsList!=null && trmsDockerNetworkSettingsList.size()>0) {
			for(TrmsDockerNetworkSettings entity:trmsDockerNetworkSettingsList) {
				//外键设置
				entity.setContainerId(trmsDockerContainer.getId());
				trmsDockerNetworkSettingsMapper.insert(entity);
			}
		}
		if(trmsDockerContainerPortsList!=null && trmsDockerContainerPortsList.size()>0) {
			for(TrmsDockerContainerPorts entity:trmsDockerContainerPortsList) {
				//外键设置
				entity.setContainerId(trmsDockerContainer.getId());
				trmsDockerContainerPortsMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		trmsDockerNetworkSettingsMapper.deleteByMainId(id);
		trmsDockerContainerPortsMapper.deleteByMainId(id);
		trmsDockerContainerMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			trmsDockerNetworkSettingsMapper.deleteByMainId(id.toString());
			trmsDockerContainerPortsMapper.deleteByMainId(id.toString());
			trmsDockerContainerMapper.deleteById(id);
		}
	}
	
}
