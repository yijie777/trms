package org.jeecg.modules.trms.service;

import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 端口映射
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface ITrmsDockerContainerPortsService extends IService<TrmsDockerContainerPorts> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TrmsDockerContainerPorts>
	 */
	public List<TrmsDockerContainerPorts> selectByMainId(String mainId);
}
