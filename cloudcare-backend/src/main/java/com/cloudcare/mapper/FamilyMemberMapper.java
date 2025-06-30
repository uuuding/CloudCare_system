package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.entity.FamilyMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 家庭成员Mapper接口
 *
 * @author CloudCare
 * @since 2024-01-01
 */
@Mapper
public interface FamilyMemberMapper extends BaseMapper<FamilyMember> {

    /**
     * 根据老人ID查询家属联系电话
     *
     * @param elderId 老人ID
     * @return 家属联系电话列表
     */
    @Select("SELECT DISTINCT fm.contact_phone " +
            "FROM family_member fm " +
            "WHERE fm.elder_id = #{elderId} " +
            "AND fm.contact_phone IS NOT NULL " +
            "AND fm.contact_phone != '' " +
            "AND fm.deleted = 0")
    List<String> getFamilyPhonesByElderId(@Param("elderId") Long elderId);

    /**
     * 根据老人ID查询家庭成员列表
     *
     * @param elderId 老人ID
     * @return 家庭成员列表
     */
    @Select("SELECT fm.*, u.real_name as user_name, u.phone as user_phone " +
            "FROM family_member fm " +
            "LEFT JOIN sys_user u ON fm.user_id = u.user_id " +
            "WHERE fm.elder_id = #{elderId} " +
            "AND fm.deleted = 0 " +
            "ORDER BY fm.relation")
    List<FamilyMember> selectByElderId(@Param("elderId") Long elderId);

    /**
     * 根据用户ID查询关联的老人列表
     *
     * @param userId 用户ID
     * @return 家庭成员列表
     */
    @Select("SELECT fm.*, ep.name as elderly_name " +
            "FROM family_member fm " +
            "LEFT JOIN elderly_profile ep ON fm.elder_id = ep.elder_id " +
            "WHERE fm.user_id = #{userId} " +
            "AND fm.deleted = 0 " +
            "ORDER BY fm.create_time DESC")
    List<FamilyMember> selectByUserId(@Param("userId") Long userId);
}