package com.cloudcare.service;

import com.cloudcare.entity.ElderlyObservations;
import java.util.List;

public interface ElderlyObservationsService {

    // 查询所有体检记录
    List<ElderlyObservations> getAllObservations();

    // 根据老人ID查询所有体检记录，按时间倒序
    List<ElderlyObservations> getObservationsByElderlyIdOrderByTimeDesc(int elderlyId);

    // 根据时间范围查询老人体检记录
    List<ElderlyObservations> getObservationsByElderlyIdAndTimeRange(int elderlyId, String startTime, String endTime);

    // 插入体检记录，包含观察地点
    boolean addObservationWithLocation(ElderlyObservations observation);

    // 更新体检记录，包含观察地点
    boolean updateObservationWithLocation(ElderlyObservations observation);

    // 删除体检记录
    boolean deleteObservation(int id);
}