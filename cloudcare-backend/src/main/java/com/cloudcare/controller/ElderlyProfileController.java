package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.ElderlyChronicDisease;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.service.ElderlyProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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
    public Result<Boolean> updateElderlyProfile(@RequestBody ElderlyProfile elderlyProfile,
            @RequestBody(required = false) List<ElderlyChronicDisease> chronicDiseases) {
        elderlyProfileService.updateProfile(elderlyProfile, chronicDiseases);
        return Result.success(true);
    }

    @PostMapping("/add")
    public Result<Boolean> addElderlyProfile(@RequestBody ElderlyProfile elderlyProfile) {
        boolean success = elderlyProfileService.addProfile(elderlyProfile);
        return Result.success(success);
    }

    @PostMapping("/addc")
    public Result<Boolean> addDis(@RequestBody ElderlyChronicDisease caseEntry) {
        boolean success = elderlyProfileService.addCaseEntry(caseEntry);
        return Result.success(success);
    }

    @GetMapping("/chronic-diseases/{elderlyId}")
    public Result<List<ElderlyChronicDisease>> getChronicDiseases(@PathVariable String elderlyId) {
        try {
            // 参数验证和转换
            if (elderlyId == null || elderlyId.trim().isEmpty() || "undefined".equals(elderlyId)
                    || "null".equals(elderlyId)) {
                return Result.error("老人ID不能为空");
            }

            Integer elderlyIdInt;
            try {
                elderlyIdInt = Integer.parseInt(elderlyId.trim());
            } catch (NumberFormatException e) {
                log.error("老人ID格式错误: {}", elderlyId);
                return Result.error("老人ID格式错误，请输入有效的数字");
            }

            if (elderlyIdInt <= 0) {
                return Result.error("老人ID必须大于0");
            }

            List<ElderlyChronicDisease> diseases = elderlyProfileService.getChronicDiseasesByElderlyId(elderlyIdInt);
            return Result.success(diseases);
        } catch (Exception e) {
            log.error("获取既往病史失败，elderlyId: {}, 错误信息: {}", elderlyId, e.getMessage(), e);
            return Result.error("获取既往病史失败: " + e.getMessage());
        }
    }

    // 删除老人档案
    @DeleteMapping("/delete/{id}")
    public boolean deleteElderlyProfile(@PathVariable int id) {
        return elderlyProfileService.deleteProfile(id);
    }
}
