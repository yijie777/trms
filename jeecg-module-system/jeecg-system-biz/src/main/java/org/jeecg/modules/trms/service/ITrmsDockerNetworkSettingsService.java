package org.jeecg.modules.trms.service;

import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 网络信息
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface ITrmsDockerNetworkSettingsService extends IService<TrmsDockerNetworkSettings> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<TrmsDockerNetworkSettings>
	 */
	public List<TrmsDockerNetworkSettings> selectByMainId(String mainId);
}
