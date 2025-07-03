package com.cloudcare.service;

import com.cloudcare.entity.ElderlyChronicDisease;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.mapper.ElderlyProfileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ElderlyProfileService {

    // 获取所有老人档案
    public List<ElderlyProfile> getAllElderlyProfiles();

    public List<ElderlyProfile> searchByNameAndAge(String name, int age);

    public boolean updateProfile(ElderlyProfile profile, List<ElderlyChronicDisease> chronicDiseases);

    public boolean deleteProfile(int id);

    public boolean addProfile(ElderlyProfile profile);

    public boolean addCaseEntry(ElderlyChronicDisease caseEntry);

    // 获取老人既往病史
    public List<ElderlyChronicDisease> getChronicDiseasesByElderlyId(Integer elderlyId);

    // 根据ID获取老人信息
    public ElderlyProfile getElderlyProfileById(Integer id);

}