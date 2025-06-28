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

    // 查询所有体检记录
    @GetMapping("/all")
    public Result<List<ElderlyObservations>> getAllObservations() {
        List<ElderlyObservations> list = observationsService.getAllObservations();
        System.out.println(list.toString() + "88888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");
        return Result.success(list);
    }

    // 根据老人ID查询所有体检记录，按时间倒序
    @GetMapping("/by-elderly/{elderlyId}")
    public Result<List<ElderlyObservations>> getObservationsByElderlyId(@PathVariable int elderlyId) {
        List<ElderlyObservations> list = observationsService.getObservationsByElderlyIdOrderByTimeDesc(elderlyId);
        return Result.success(list);
    }

    // 根据时间范围查询老人体检记录
    @GetMapping("/by-elderly/{elderlyId}/range")
    public Result<List<ElderlyObservations>> getObservationsByTimeRange(@PathVariable int elderlyId,
                                                                       @RequestParam String startTime,
                                                                       @RequestParam String endTime) {
        List<ElderlyObservations> list = observationsService.getObservationsByElderlyIdAndTimeRange(elderlyId, startTime, endTime);
        return Result.success(list);
    }

    // 新增体检记录，包含观察地点
    @PostMapping("/add")
    public Result<Boolean> addObservation(@RequestBody ElderlyObservations observation) {
        boolean success = observationsService.addObservationWithLocation(observation);
        if (success) {
            try {
                // 自动触发预警检查
                healthAlertService.checkAndGenerateAlerts(observation);
                System.out.println("预警66666666666666666666666666666666666666666666666666666666666666666666666666666");
                log.info("观察记录添加成功并完成预警检查，老人ID: {}", observation.getElderlyId());
            } catch (Exception e) {
                log.error("预警检查失败，老人ID: {}, 错误: {}", observation.getElderlyId(), e.getMessage());
            }
            return Result.success(true);
        } else {
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