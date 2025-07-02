package com.cloudcare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloudcare.entity.GpsAlarm;
import com.cloudcare.mapper.GpsAlarmMapper;
import com.cloudcare.service.GpsAlarmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * GPS报警服务实现类
 *
 * @author CloudCare
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GpsAlarmServiceImpl extends ServiceImpl<GpsAlarmMapper, GpsAlarm> implements GpsAlarmService {

    private final GpsAlarmMapper gpsAlarmMapper;
    
    @Override
    public Long getUnhandledCountByImei(String deviceImei) {
        LambdaQueryWrapper<GpsAlarm> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GpsAlarm::getMacid, deviceImei)
               .eq(GpsAlarm::getHandleStatus, 0);
        return this.count(wrapper);
    }
    
    @Override
    public Long getUnhandledCountByElderlyId(Long elderlyId) {
        LambdaQueryWrapper<GpsAlarm> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(GpsAlarm::getElderlyId, elderlyId)
               .eq(GpsAlarm::getHandleStatus, 0);
        return this.count(wrapper);
    }
    
    @Override
    public boolean processAlarm(Long alarmId, String processNotes) {
        try {
            GpsAlarm alarm = this.getById(alarmId);
            if (alarm != null) {
                alarm.setHandleStatus(1);
                alarm.setHandleRemark(processNotes);
                alarm.setHandleTime(LocalDateTime.now());
                return this.updateById(alarm);
            }
            return false;
        } catch (Exception e) {
            log.error("处理报警时发生错误", e);
            return false;
        }
    }
    
    @Override
    public Object getAlarmStatistics() {
        try {
            Long totalCount = this.count();
            
            LambdaQueryWrapper<GpsAlarm> unhandledWrapper = new LambdaQueryWrapper<>();
            unhandledWrapper.eq(GpsAlarm::getHandleStatus, 0);
            Long unhandledCount = this.count(unhandledWrapper);
            
            Long handledCount = totalCount - unhandledCount;
            
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalCount", totalCount);
            statistics.put("unhandledCount", unhandledCount);
            statistics.put("handledCount", handledCount);
            
            return statistics;
        } catch (Exception e) {
            log.error("获取报警统计时发生错误", e);
            return null;
        }
    }
}