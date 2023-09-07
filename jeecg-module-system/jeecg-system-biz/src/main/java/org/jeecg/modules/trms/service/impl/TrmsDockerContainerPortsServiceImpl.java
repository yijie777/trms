package org.jeecg.modules.trms.service.impl;

import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import org.jeecg.modules.trms.mapper.TrmsDockerContainerPortsMapper;
import org.jeecg.modules.trms.service.ITrmsDockerContainerPortsService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 端口映射
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
@Service
public class TrmsDockerContainerPortsServiceImpl extends ServiceImpl<TrmsDockerContainerPortsMapper, TrmsDockerContainerPorts> implements ITrmsDockerContainerPortsService {
	
	@Autowired
	private TrmsDockerContainerPortsMapper trmsDockerContainerPortsMapper;
	
	@Override
	public List<TrmsDockerContainerPorts> selectByMainId(String mainId) {
		return trmsDockerContainerPortsMapper.selectByMainId(mainId);
	}
}
