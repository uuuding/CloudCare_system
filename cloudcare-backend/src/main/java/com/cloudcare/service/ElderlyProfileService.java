package com.cloudcare.service;

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

    public boolean updateProfile(ElderlyProfile profile);

    public boolean deleteProfile(int id);

}