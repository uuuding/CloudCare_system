package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.ElderlyObservations;
import com.cloudcare.service.ElderlyObservationsService;
import com.cloudcare.service.HealthAlertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/elderly-observations")
@CrossOrigin(origins = "*")
public class ElderlyObservationsController {

    @Autowired
    private ElderlyObservationsService observationsService;

    @Autowired
    private HealthAlertService healthAlertService;
    
    @Autowired
    private com.cloudcare.service.SystemLogService systemLogService;

    // 查询所有体检记录
    @GetMapping("/all")
    public Result<List<ElderlyObservations>> getAllObservations() {
        List<ElderlyObservations> list = observationsService.getAllObservations();
        System.out.println(list.toString() + "8888888");
        return Result.success(list);
    }

    // 根据老人ID查询所有体检记录，按时间倒序
    @GetMapping("/by-elderly/{elderlyId}")
    public Result<List<ElderlyObservations>> getObservationsByElderlyId(@PathVariable String elderlyId) {
        try {
            // 参数验证和转换
            if (elderlyId == null || elderlyId.trim().isEmpty() || "undefined".equals(elderlyId)
                    || "null".equals(elderlyId)) {
                return Result.error("老人ID不能为空");
            }

            int elderlyIdInt;
            try {
                elderlyIdInt = Integer.parseInt(elderlyId.trim());
            } catch (NumberFormatException e) {
                log.error("老人ID格式错误: {}", elderlyId);
                return Result.error("老人ID格式错误，请输入有效的数字");
            }

            if (elderlyIdInt <= 0) {
                return Result.error("老人ID必须大于0");
            }

            List<ElderlyObservations> list = observationsService
                    .getObservationsByElderlyIdOrderByTimeDesc(elderlyIdInt);
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取体检记录失败，elderlyId: {}, 错误信息: {}", elderlyId, e.getMessage(), e);
            return Result.error("获取体检记录失败: " + e.getMessage());
        }
    }

    // 根据时间范围查询老人体检记录
    @GetMapping("/by-elderly/{elderlyId}/range")
    public Result<List<ElderlyObservations>> getObservationsByTimeRange(@PathVariable String elderlyId,
            @RequestParam String startTime,
            @RequestParam String endTime) {
        try {
            // 参数验证和转换
            if (elderlyId == null || elderlyId.trim().isEmpty() || "undefined".equals(elderlyId)
                    || "null".equals(elderlyId)) {
                return Result.error("老人ID不能为空");
            }

            int elderlyIdInt;
            try {
                elderlyIdInt = Integer.parseInt(elderlyId.trim());
            } catch (NumberFormatException e) {
                log.error("老人ID格式错误: {}", elderlyId);
                return Result.error("老人ID格式错误，请输入有效的数字");
            }

            if (elderlyIdInt <= 0) {
                return Result.error("老人ID必须大于0");
            }

            List<ElderlyObservations> list = observationsService.getObservationsByElderlyIdAndTimeRange(elderlyIdInt,
                    startTime, endTime);
            return Result.success(list);
        } catch (Exception e) {
            log.error("根据时间范围查询体检记录失败，elderlyId: {}, startTime: {}, endTime: {}, 错误信息: {}", elderlyId, startTime,
                    endTime, e.getMessage(), e);
            return Result.error("查询体检记录失败: " + e.getMessage());
        }
    }

    // 新增体检记录，包含观察地点
    @PostMapping("/add")
    public Result<Boolean> addObservation(@RequestBody ElderlyObservations observation) {
        boolean success = observationsService.addObservationWithLocation(observation);
        if (success) {
            try {
                // 自动触发预警检查
                healthAlertService.checkAndGenerateAlerts(observation);
                log.info("观察记录添加成功并完成预警检查，老人ID: {}", observation.getElderlyId());
                
                // 记录系统日志
                String logContent = String.format("新增健康评估记录，老人ID: %d，体温: %.1f℃，收缩压: %d mmHg，心率: %d bpm", 
                    observation.getElderlyId(), 
                    observation.getBodyTemperature(),
                    observation.getSystolicBp(),
                    observation.getHeartRate());
                
                systemLogService.saveLog("INFO", "HEALTH", "新增健康评估", logContent);
            } catch (Exception e) {
                log.error("预警检查失败，老人ID: {}, 错误: {}", observation.getElderlyId(), e.getMessage());
            }
            return Result.success(true);
        } else {
            // 记录失败日志
            systemLogService.saveErrorLog("HEALTH", "新增健康评估失败", 
                "添加健康评估记录失败，老人ID: " + observation.getElderlyId(), null);
            return Result.error("添加失败");
        }
    }

    // 更新体检记录，包含观察地点
    @PutMapping("/update")
    public Result<Boolean> updateObservation(@RequestBody ElderlyObservations observation) {
        boolean success = observationsService.updateObservationWithLocation(observation);
        return Result.success(success);
    }

    // 删除体检记录
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> deleteObservation(@PathVariable int id) {
        boolean success = observationsService.deleteObservation(id);
        return Result.success(success);
    }
}