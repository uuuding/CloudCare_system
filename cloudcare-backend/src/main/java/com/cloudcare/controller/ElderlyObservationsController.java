package com.cloudcare.controller;

import com.cloudcare.common.Result;
import com.cloudcare.entity.ElderlyObservations;
import com.cloudcare.service.ElderlyObservationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/elderly-observations")
@CrossOrigin
public class ElderlyObservationsController {

    @Autowired
    private ElderlyObservationsService observationsService;

    // 查询所有体检记录
    @GetMapping("/all")
    public Result<List<ElderlyObservations>> getAllObservations() {
        List<ElderlyObservations> list = observationsService.getAllObservations();
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
        return Result.success(success);
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