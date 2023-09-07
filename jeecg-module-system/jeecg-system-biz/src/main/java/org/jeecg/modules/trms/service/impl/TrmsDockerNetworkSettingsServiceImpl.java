package org.jeecg.modules.trms.service.impl;

import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import org.jeecg.modules.trms.mapper.TrmsDockerNetworkSettingsMapper;
import org.jeecg.modules.trms.service.ITrmsDockerNetworkSettingsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 网络信息
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Service
public class TrmsDockerNetworkSettingsServiceImpl extends ServiceImpl<TrmsDockerNetworkSettingsMapper, TrmsDockerNetworkSettings> implements ITrmsDockerNetworkSettingsService {
	
	@Autowired
	private TrmsDockerNetworkSettingsMapper trmsDockerNetworkSettingsMapper;
	
	@Override
	public List<TrmsDockerNetworkSettings> selectByMainId(String mainId) {
		return trmsDockerNetworkSettingsMapper.selectByMainId(mainId);
	}
}
