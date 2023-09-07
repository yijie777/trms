package org.jeecg.modules.trms.mapper;

import java.util.List;
import org.jeecg.modules.trms.entity.TrmsDockerContainerPorts;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 端口映射
 * @Author: jeecg-boot
 * @Date:   2023-09-06
 * @Version: V1.0
 */
public interface TrmsDockerContainerPortsMapper extends BaseMapper<TrmsDockerContainerPorts> {

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
   * @return List<TrmsDockerContainerPorts>
   */
	public List<TrmsDockerContainerPorts> selectByMainId(@Param("mainId") String mainId);
}
