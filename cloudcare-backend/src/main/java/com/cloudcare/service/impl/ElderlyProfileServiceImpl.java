package com.cloudcare.service.impl;

import com.cloudcare.entity.ElderlyChronicDisease;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.mapper.ElderlyProfileMapper;
import com.cloudcare.mapper.HealthAlertMapper;
import com.cloudcare.mapper.ElderlyObservationsMapper;
import com.cloudcare.service.ElderlyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ElderlyProfileServiceImpl implements ElderlyProfileService {

    @Autowired
    private ElderlyProfileMapper elderlyProfileMapper;
    
    @Autowired
    private HealthAlertMapper healthAlertMapper;
    
    @Autowired
    private ElderlyObservationsMapper elderlyObservationsMapper;

    @Override
    public List<ElderlyProfile> getAllElderlyProfiles() {
        return elderlyProfileMapper.selectList(null); // 使用 MyBatis-Plus 获取所有记录
    }

    @Override
    public List<ElderlyProfile> searchByNameAndAge(String name, int age) {
        return elderlyProfileMapper.searchByNameAndAge(name, age);
    }

    @Override
    public boolean updateProfile(ElderlyProfile profile, List<ElderlyChronicDisease> chronicDiseases) {
        // 先更新老人档案基本信息
        boolean updated = elderlyProfileMapper.updateProfile(profile);
        if (updated) {
            // 删除原有既往病史
            // 插入新的既往病史
            if (chronicDiseases != null && !chronicDiseases.isEmpty()) {
                elderlyProfileMapper.deleteChronicDiseasesByElderlyId(profile.getId());
                elderlyProfileMapper.insertChronicDiseases(profile.getId(), chronicDiseases);
            }
        }
        return updated;
    }

    @Override
    @Transactional
    public boolean deleteProfile(int id) {
        try {
            // 删除关联的健康预警记录
            healthAlertMapper.deleteAlertsByElderlyId(id);
            
            // 删除关联的健康评估记录（体检记录）
            elderlyObservationsMapper.deleteObservationsByElderlyId(id);
            
            // 删除慢性病记录
            elderlyProfileMapper.deleteChronicDiseasesByElderlyId(id);
            
            // 删除老人档案
            return elderlyProfileMapper.deleteProfile(id);
        } catch (Exception e) {
            throw new RuntimeException("删除老人档案失败: " + e.getMessage(), e);
        }
    }

@Override
    public boolean addProfile(ElderlyProfile profile) {
        return elderlyProfileMapper.insertProfile(profile) > 0;
    }

@Override
    public boolean addCaseEntry(ElderlyChronicDisease caseEntry) {
        return elderlyProfileMapper.insertCaseEntry(caseEntry) > 0;
    }

@Override
    public List<ElderlyChronicDisease> getChronicDiseasesByElderlyId(Integer elderlyId) {
        return elderlyProfileMapper.selectChronicDiseasesByElderlyId(elderlyId);
    }

    @Override
    public ElderlyProfile getElderlyProfileById(Integer id) {
        return elderlyProfileMapper.selectById(id);
    }
}
