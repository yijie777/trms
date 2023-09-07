package org.jeecg.modules.trms.mapper;

import java.util.List;
import org.jeecg.modules.trms.entity.TrmsDockerNetworkSettings;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 网络信息
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface TrmsDockerNetworkSettingsMapper extends BaseMapper<TrmsDockerNetworkSettings> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<TrmsDockerNetworkSettings>
   */
	public List<TrmsDockerNetworkSettings> selectByMainId(@Param("mainId") String mainId);
}
