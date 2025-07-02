package com.cloudcare.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.cloudcare.entity.GpsAlarm;
import com.cloudcare.entity.GpsLocation;
import com.cloudcare.service.GpsDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * GPS数据接收控制器
 *
 * @author CloudCare
 */
@Slf4j
@RestController
@RequestMapping("/api/gps")
@RequiredArgsConstructor
public class GpsDataController {

    private final GpsDataService gpsDataService;

    /**
     * 接收GPS报警数据
     */
    @PostMapping("/alarm")
    public String receiveAlarmData(HttpServletRequest request) {
        try {
            String method = request.getParameter("method");
            String serialNumber = request.getParameter("serialNumber");
            String data = request.getParameter("data");

            log.info("接收到GPS报警数据: method={}, serialNumber={}, data={}", method, serialNumber, data);

            if ("alarm".equals(method) && data != null) {
                JSONArray dataArray = JSON.parseArray(data);
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject alarmData = dataArray.getJSONObject(i);
                    GpsAlarm alarm = parseAlarmData(alarmData);
                    gpsDataService.saveAlarmData(alarm);
                }
            }

            // 返回流水号表示接收成功
            return serialNumber;
        } catch (Exception e) {
            log.error("处理GPS报警数据失败", e);
            return "error";
        }
    }

    /**
     * 接收GPS位置状态数据
     */
    @PostMapping("/status")
    public String receiveStatusData(HttpServletRequest request) {
        try {
            String method = request.getParameter("method");
            String serialNumber = request.getParameter("serialNumber");
            String data = request.getParameter("data");

            log.info("接收到GPS状态数据: method={}, serialNumber={}, data={}", method, serialNumber, data);

            if ("status".equals(method) && data != null) {
                JSONArray dataArray = JSON.parseArray(data);
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject statusData = dataArray.getJSONObject(i);
                    GpsLocation location = parseStatusData(statusData);
                    gpsDataService.saveLocationData(location);
                }
            }

            // 返回流水号表示接收成功
            return serialNumber;
        } catch (Exception e) {
            log.error("处理GPS状态数据失败", e);
            return "error";
        }
    }

    /**
     * 接收多媒体数据
     */
    @PostMapping("/media")
    public String receiveMediaData(HttpServletRequest request) {
        try {
            String method = request.getParameter("method");
            String serialNumber = request.getParameter("serialNumber");
            String data = request.getParameter("data");

            log.info("接收到GPS多媒体数据: method={}, serialNumber={}, data={}", method, serialNumber, data);

            if (("pic".equals(method) || "voice".equals(method) || "video".equals(method)) && data != null) {
                JSONArray dataArray = JSON.parseArray(data);
                for (int i = 0; i < dataArray.size(); i++) {
                    JSONObject mediaData = dataArray.getJSONObject(i);
                    gpsDataService.saveMediaData(method, mediaData);
                }
            }

            // 返回流水号表示接收成功
            return serialNumber;
        } catch (Exception e) {
            log.error("处理GPS多媒体数据失败", e);
            return "error";
        }
    }

    /**
     * 测试推送接口
     */
    @RequestMapping("/test")
    public String testPush(HttpServletRequest request,
                          @RequestParam(required = false) String callback) {
        try {
            String method = request.getParameter("method");
            String serialNumber = request.getParameter("serialNumber");

            log.info("接收到GPS测试推送: method={}, serialNumber={}, callback={}", method, serialNumber, callback);

            if ("PushTest".equals(method)) {
                // JSONP请求
                if (callback != null && !callback.isEmpty()) {
                    return callback + "(\"" + serialNumber + "\")";
                }
                // 普通请求
                return serialNumber;
            }

            return "error";
        } catch (Exception e) {
            log.error("处理GPS测试推送失败", e);
            return "error";
        }
    }

    /**
     * 解析报警数据
     */
    private GpsAlarm parseAlarmData(JSONObject data) {
        GpsAlarm alarm = new GpsAlarm();
        alarm.setAlarmId(data.getString("Id"));
        alarm.setMacid(data.getString("Macid"));
        alarm.setDeviceName(data.getString("FullName"));
        alarm.setAlarmTime(data.getLong("PTime"));
        alarm.setAddTime(data.getLong("AddTime"));
        alarm.setLat(data.getDouble("Lat"));
        alarm.setLon(data.getDouble("Lon"));
        alarm.setMapLat(data.getDouble("MapLat"));
        alarm.setMapLon(data.getDouble("MapLon"));
        alarm.setSpeed(data.getDouble("Speed"));
        alarm.setDirection(data.getInteger("Dir"));
        alarm.setClassify(data.getInteger("Classify"));
        alarm.setDescribeInfo(data.getString("Describe"));
        alarm.setNote(data.getString("Notea"));
        alarm.setHandleStatus(0); // 默认未处理
        return alarm;
    }

    /**
     * 解析状态数据
     */
    private GpsLocation parseStatusData(JSONObject data) {
        GpsLocation location = new GpsLocation();
        location.setMacid(data.getString("Macid"));
        location.setGpsTime(data.getLong("GpsTime"));
        location.setHeartTime(data.getLong("HeartTime"));
        location.setUpdTime(data.getLong("UpdTime"));
        location.setSpeed(data.getDouble("Speed"));
        location.setDirection(data.getDouble("Dir"));
        location.setLat(data.getDouble("Lat"));
        location.setLon(data.getDouble("Lon"));
        location.setMapLat(data.getDouble("MapLat"));
        location.setMapLon(data.getDouble("MapLon"));
        location.setStats(data.getString("Stats"));
        location.setValue(data.getString("Value"));
        
        // 解析信号类型
        String value = data.getString("Value");
        if (value != null && !value.isEmpty()) {
            String[] values = value.split(",");
            if (values.length > 15) {
                try {
                    location.setSignalType(Integer.parseInt(values[15]));
                } catch (NumberFormatException e) {
                    location.setSignalType(0); // 默认GPS
                }
            }
        }
        
        return location;
    }
}