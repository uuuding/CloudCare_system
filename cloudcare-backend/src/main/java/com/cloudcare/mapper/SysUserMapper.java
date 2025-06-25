package com.cloudcare.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cloudcare.domain.SysUser;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 系统用户 数据层
 *
 * @author CloudCare
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询指定日期之前创建的用户数量
     *
     * @param date 日期
     * @return 用户数量
     */
    int selectCountByCreateTimeBefore(@Param("date") LocalDate date);
}