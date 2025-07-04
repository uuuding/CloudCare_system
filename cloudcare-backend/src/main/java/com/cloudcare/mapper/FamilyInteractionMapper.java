package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cloudcare.dto.FamilyInteractionDTO;
import com.cloudcare.entity.FamilyInteraction;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 家属互动Mapper接口
 */
@Mapper
public interface FamilyInteractionMapper extends BaseMapper<FamilyInteraction> {

    /**
     * 分页查询家属互动记录（包含关联信息）
     */
    @Select("SELECT fi.*, " +
            "u1.real_name as elder_name, " +
            "u2.real_name as family_name, " +
            "fm.relation as family_relation " +
            "FROM family_interaction fi " +
            "LEFT JOIN sys_user u1 ON fi.elder_id = u1.user_id " +
            "LEFT JOIN family_member fm ON fi.family_member_id = fm.id " +
            "LEFT JOIN sys_user u2 ON fm.user_id = u2.user_id " +
            "WHERE (#{elderId} IS NULL OR fi.elder_id = #{elderId}) " +
            "AND (#{familyMemberId} IS NULL OR fi.family_member_id = #{familyMemberId}) " +
            "AND (#{interactionType} IS NULL OR #{interactionType} = '' OR fi.interaction_type = #{interactionType}) " +
            "AND (#{status} IS NULL OR #{status} = '' OR fi.status = #{status}) " +
            "AND (#{startTime} IS NULL OR fi.interaction_time >= #{startTime}) " +
            "AND (#{endTime} IS NULL OR fi.interaction_time <= #{endTime}) " +
            "ORDER BY fi.interaction_time DESC")
    IPage<FamilyInteractionDTO> selectPageWithDetails(
            Page<FamilyInteractionDTO> page,
            @Param("elderId") Long elderId,
            @Param("familyMemberId") Long familyMemberId,
            @Param("interactionType") String interactionType,
            @Param("status") String status,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据ID查询家属互动记录（包含关联信息）
     */
    @Select("SELECT fi.*, " +
            "u1.real_name as elder_name, " +
            "u2.real_name as family_name, " +
            "fm.relation as family_relation " +
            "FROM family_interaction fi " +
            "LEFT JOIN sys_user u1 ON fi.elder_id = u1.user_id " +
            "LEFT JOIN family_member fm ON fi.family_member_id = fm.id " +
            "LEFT JOIN sys_user u2 ON fm.user_id = u2.user_id " +
            "WHERE fi.id = #{id}")
    FamilyInteractionDTO selectByIdWithDetails(@Param("id") Long id);

    /**
     * 获取互动统计数据
     */
    @Select("SELECT " +
            "COUNT(1) as total_count, " +
            "COUNT(CASE WHEN status = 'pending' THEN 1 END) as pending_count, " +
            "COUNT(CASE WHEN status = 'replied' THEN 1 END) as replied_count, " +
            "COUNT(CASE WHEN status = 'closed' THEN 1 END) as closed_count, " +
            "COUNT(CASE WHEN DATE(interaction_time) = CURDATE() THEN 1 END) as today_count " +
            "FROM family_interaction")
    Map<String, Object> getInteractionStats();

    /**
     * 获取老人的家属列表
     */
    @Select("SELECT fm.id, fm.relation, u.real_name as name " +
            "FROM family_member fm " +
            "LEFT JOIN sys_user u ON fm.user_id = u.user_id " +
            "WHERE fm.elder_id = #{elderId}")
    List<Map<String, Object>> getFamilyMembersByElderId(@Param("elderId") Long elderId);
    
    /**
     * 根据老人ID删除所有家属互动记录
     */
    @Delete("DELETE FROM family_interaction WHERE elder_id = #{elderId}")
    int deleteInteractionsByElderId(@Param("elderId") int elderId);
}