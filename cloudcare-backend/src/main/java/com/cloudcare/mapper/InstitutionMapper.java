package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.Institution;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 机构信息Mapper接口
 *
 * @author cloudcare
 */
@Mapper
public interface InstitutionMapper extends BaseMapper<Institution> {

    /**
     * 获取机构统计信息
     */
    Map<String, Object> getInstitutionStatistics();

    /**
     * 获取床位统计信息
     */
    Map<String, Object> getBedStatistics();

    /**
     * 根据名称模糊查询机构
     */
    List<Institution> selectByNameLike(String name);

    /**
     * 获取入住率排行榜
     */
    List<Map<String, Object>> getOccupancyRanking(int limit);
}