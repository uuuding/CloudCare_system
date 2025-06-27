package com.cloudcare.service.impl;

import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.mapper.ElderlyProfileMapper;
import com.cloudcare.service.ElderlyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElderlyProfileServiceImpl implements ElderlyProfileService {

    @Autowired
    private ElderlyProfileMapper elderlyProfileMapper;

    @Override
    public List<ElderlyProfile> getAllElderlyProfiles() {
        return elderlyProfileMapper.selectList(null); // 使用 MyBatis-Plus 获取所有记录
    }

    @Override
    public List<ElderlyProfile> searchByNameAndAge(String name, int age) {
        return elderlyProfileMapper.searchByNameAndAge(name, age);
    }

    @Override
    public boolean updateProfile(ElderlyProfile profile) {
        return elderlyProfileMapper.updateProfile(profile);
    }

    @Override
    public boolean deleteProfile(int id) {
        return elderlyProfileMapper.deleteProfile(id);
    }
}
