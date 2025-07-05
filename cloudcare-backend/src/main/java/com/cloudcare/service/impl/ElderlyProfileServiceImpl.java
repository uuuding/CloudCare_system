package com.cloudcare.service.impl;

import com.cloudcare.dto.BatchElderlyProfileDTO;
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
    @Transactional
    public int batchAddProfiles(List<ElderlyProfile> profiles) {
        int successCount = 0;
        for (ElderlyProfile profile : profiles) {
            try {
                // 数据验证
                if (profile.getName() == null || profile.getName().trim().isEmpty()) {
                    continue; // 跳过无效数据
                }
                if (profile.getAge() == null || profile.getAge() <= 0 || profile.getAge() > 150) {
                    continue; // 跳过无效年龄
                }
                if (profile.getGender() == null || profile.getGender().trim().isEmpty()) {
                    continue; // 跳过无效性别
                }
                
                // 设置创建和更新时间
                String currentTime = java.time.LocalDateTime.now().toString();
                profile.setCreateTime(currentTime);
                profile.setUpdateTime(currentTime);
                
                if (elderlyProfileMapper.insertProfile(profile) > 0) {
                    successCount++;
                }
            } catch (Exception e) {
                // 记录错误但继续处理其他记录
                System.err.println("批量新增老人档案时出错: " + e.getMessage());
            }
        }
        return successCount;
    }

    @Override
    @Transactional
    public int batchAddProfilesWithDisease(List<BatchElderlyProfileDTO> batchProfiles) {
        int successCount = 0;
        for (BatchElderlyProfileDTO batchProfile : batchProfiles) {
            try {
                // 数据验证
                if (batchProfile.getName() == null || batchProfile.getName().trim().isEmpty()) {
                    continue;
                }
                if (batchProfile.getAge() == null || batchProfile.getAge() <= 0 || batchProfile.getAge() > 150) {
                    continue;
                }
                if (batchProfile.getGender() == null || batchProfile.getGender().trim().isEmpty()) {
                    continue;
                }
                
                // 创建老人档案对象
                ElderlyProfile profile = new ElderlyProfile();
                profile.setName(batchProfile.getName().trim());
                profile.setAge(batchProfile.getAge());
                profile.setGender(batchProfile.getGender());
                String currentTime = java.time.LocalDateTime.now().toString();
                profile.setCreateTime(currentTime);
                profile.setUpdateTime(currentTime);
                
                // 插入老人档案
                int result = elderlyProfileMapper.insertProfile(profile);
                if (result > 0) {
                    // 如果有病例信息，则插入病例记录
                    if (batchProfile.getChronicDiseases() != null && !batchProfile.getChronicDiseases().isEmpty()) {
                        for (BatchElderlyProfileDTO.ChronicDiseaseInfo diseaseInfo : batchProfile.getChronicDiseases()) {
                            if (diseaseInfo.getDiseaseName() != null && !diseaseInfo.getDiseaseName().trim().isEmpty()) {
                                try {
                                    ElderlyChronicDisease disease = new ElderlyChronicDisease();
                                    disease.setElderlyId(profile.getId());
                                    disease.setDiseaseName(diseaseInfo.getDiseaseName().trim());
                                    disease.setDiseaseCategory(diseaseInfo.getDiseaseCategory());
                                    disease.setDiagnosisDate(diseaseInfo.getDiagnosisDate());
                                    
                                    elderlyProfileMapper.insertChronicDisease(disease);
                                } catch (Exception e) {
                                    System.err.println("添加病例信息失败，老人ID: " + profile.getId() + ", 疾病: " + diseaseInfo.getDiseaseName() + ", 错误: " + e.getMessage());
                                }
                            }
                        }
                    }
                    successCount++;
                }
            } catch (Exception e) {
                System.err.println("批量新增失败，姓名: " + batchProfile.getName() + ", 错误: " + e.getMessage());
            }
        }
        return successCount;
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
