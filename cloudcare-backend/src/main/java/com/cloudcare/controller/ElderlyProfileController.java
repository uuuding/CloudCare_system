package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.service.ElderlyProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elderly-profile")
public class ElderlyProfileController {

    @Autowired
    private ElderlyProfileService elderlyProfileService;

    // 查询所有老人档案
    @GetMapping("/index")
    public Result<List<ElderlyProfile>> getAllElderlyProfiles() {
        List<ElderlyProfile> elderlyProfiles = elderlyProfileService.getAllElderlyProfiles();
        return Result.success(elderlyProfiles); // 使用 Result 封装返回数据
    }

    // 根据条件查询老人档案
    @GetMapping("/search")
    public List<ElderlyProfile> searchElderlyProfiles(String name, int age) {
        return elderlyProfileService.searchByNameAndAge(name, age);
    }

    // 更新老人档案
    @PutMapping("/update")
    public boolean updateElderlyProfile(@RequestBody ElderlyProfile elderlyProfile) {
        return elderlyProfileService.updateProfile(elderlyProfile);
    }

    // 删除老人档案
    @DeleteMapping("/delete/{id}")
    public boolean deleteElderlyProfile(@PathVariable int id) {
        return elderlyProfileService.deleteProfile(id);
    }
}
