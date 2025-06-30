package com.cloudcare.service.impl;

import com.cloudcare.entity.ElderlyObservations;
import com.cloudcare.entity.ElderlyProfile;
import com.cloudcare.entity.HealthAlert;
import com.cloudcare.entity.HealthAlertRule;
import com.cloudcare.mapper.ElderlyObservationsMapper;
import com.cloudcare.mapper.ElderlyProfileMapper;
import com.cloudcare.mapper.HealthAlertMapper;
import com.cloudcare.mapper.HealthAlertRuleMapper;
import com.cloudcare.service.HealthAlertService;
import com.cloudcare.service.InterventionPlanService;
import com.cloudcare.service.SmsNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.*;

/**
 * 健康预警服务实现类
 */
@Slf4j
@Service
public class HealthAlertServiceImpl implements HealthAlertService {
    
    @Autowired
    private HealthAlertMapper healthAlertMapper;
    
    @Autowired
    private HealthAlertRuleMapper healthAlertRuleMapper;
    
    @Autowired
    private ElderlyProfileMapper elderlyProfileMapper;
    
    @Autowired
    private ElderlyObservationsMapper elderlyObservationsMapper;
    
    @Autowired
    private InterventionPlanService interventionPlanService;
    
    @Autowired
    private SmsNotificationService smsNotificationService;
    
    @Override
    @Transactional
    public void checkAndGenerateAlerts(ElderlyObservations observation) {
        try {
            // 获取老人信息
            ElderlyProfile elderly = elderlyProfileMapper.selectById(observation.getElderlyId());
            if (elderly == null) {
                log.warn("未找到老人信息，ID: {}", observation.getElderlyId());
                return;
            }
            System.out.println("laooooooooooooooo8888888888888888888888888888888888888888888" + elderly.toString());
            
            // 计算年龄
            Integer age = elderly.getAge();
            String gender = elderly.getGender();
            
            // 检查各项指标
            checkTemperature(observation, elderly, age, gender);
            checkBloodPressure(observation, elderly, age, gender);
            checkHeartRate(observation, elderly, age, gender);
            checkBMI(observation, elderly, age, gender);
            checkSleepHours(observation, elderly, age, gender);
            
        } catch (Exception e) {
            log.error("检查健康预警时发生错误", e);
        }
    }
    
    private void checkTemperature(ElderlyObservations observation, ElderlyProfile elderly, Integer age, String gender) {
        if (observation.getBodyTemperature() == null) return;
        
        List<HealthAlertRule> rules = healthAlertRuleMapper.findApplicableRules("TEMPERATURE");
        
        for (HealthAlertRule rule : rules) {
            if (isValueOutOfRange(observation.getBodyTemperature(), rule.getMinThreshold(), rule.getMaxThreshold())) {
                createAlert(observation, elderly, "TEMPERATURE", rule, 
                    observation.getBodyTemperature().toString() + "°C", 
                    "36.0-37.5°C");
                break; // 只创建最高级别的预警
            }
        }
    }
    
    private void checkBloodPressure(ElderlyObservations observation, ElderlyProfile elderly, Integer age, String gender) {
        // 只检查收缩压（最高血压）
        if (observation.getSystolicBp() != null) {
            List<HealthAlertRule> systolicRules = healthAlertRuleMapper.findApplicableRules("BLOOD_PRESSURE_SYSTOLIC");
            for (HealthAlertRule rule : systolicRules) {
                if (isValueOutOfRange(observation.getSystolicBp().doubleValue(), rule.getMinThreshold(), rule.getMaxThreshold())) {
                    createAlert(observation, elderly, "BLOOD_PRESSURE", rule, 
                        observation.getSystolicBp().toString() + "mmHg", 
                        "90-140mmHg");
                    break;
                }
            }
        }
    }
    
    private void checkHeartRate(ElderlyObservations observation, ElderlyProfile elderly, Integer age, String gender) {
        if (observation.getHeartRate() == null) return;
        
        List<HealthAlertRule> rules = healthAlertRuleMapper.findApplicableRules("HEART_RATE");
        for (HealthAlertRule rule : rules) {
            if (isValueOutOfRange(observation.getHeartRate().doubleValue(), rule.getMinThreshold(), rule.getMaxThreshold())) {
                createAlert(observation, elderly, "HEART_RATE", rule, 
                    observation.getHeartRate().toString() + "次/分", 
                    "60-100次/分");
                break;
            }
        }
    }
    
    private void checkBMI(ElderlyObservations observation, ElderlyProfile elderly, Integer age, String gender) {
        if (observation.getHeight() == null || observation.getWeight() == null) return;
        
        double bmi = calculateBMI(observation.getHeight(), observation.getWeight());
        List<HealthAlertRule> rules = healthAlertRuleMapper.findApplicableRules("BMI");
        for (HealthAlertRule rule : rules) {
            if (isValueOutOfRange(bmi, rule.getMinThreshold(), rule.getMaxThreshold())) {
                createAlert(observation, elderly, "BMI", rule, 
                    String.format("%.1f", bmi), 
                    "18.5-24.9");
                break;
            }
        }
    }
    
    private void checkSleepHours(ElderlyObservations observation, ElderlyProfile elderly, Integer age, String gender) {
        if (observation.getSleepHours() == null) return;
        
        List<HealthAlertRule> rules = healthAlertRuleMapper.findApplicableRules("SLEEP");
        for (HealthAlertRule rule : rules) {
            if (isValueOutOfRange(observation.getSleepHours(), rule.getMinThreshold(), rule.getMaxThreshold())) {

                createAlert(observation, elderly, "SLEEP", rule, 
                    observation.getSleepHours().toString() + "小时",
                    "7-9小时");
                break;
            }
        }
    }
    
    private boolean isValueOutOfRange(Double value, Double minThreshold, Double maxThreshold) {
        // 如果只有最小阈值，表示值应该大于等于最小阈值
        if (minThreshold != null && maxThreshold == null) {
            return value < minThreshold;
        }
        // 如果只有最大阈值，表示值应该小于等于最大阈值
        if (minThreshold == null && maxThreshold != null) {
            return value > maxThreshold;
        }
        // 如果有范围阈值，表示值应该在范围内
        if (minThreshold != null && maxThreshold != null) {
            return value < minThreshold || value > maxThreshold;
        }
        // 如果都没有阈值，则不算异常
        return false;
    }
    
    private void createAlert(ElderlyObservations observation, ElderlyProfile elderly, String alertType, 
                           HealthAlertRule rule, String triggerValue, String normalRange) {
        // 检查是否已存在相同类型的活跃预警
        HealthAlert existingAlert = healthAlertMapper.findLatestByElderlyIdAndType(
            observation.getElderlyId(), alertType);
        if (existingAlert != null) {
            log.info("老人{}已存在{}类型的活跃预警，跳过创建", elderly.getName(), alertType);
            return;
        }
        
        HealthAlert alert = new HealthAlert();
        alert.setElderlyId(observation.getElderlyId());
        alert.setElderlyName(elderly.getName());
        alert.setAlertType(alertType);
        alert.setAlertLevel(rule.getAlertLevel());
        alert.setAlertTitle(generateAlertTitle(alertType, rule.getAlertLevel()));
        alert.setAlertDescription(rule.getMessageTemplate().replace("{value}", triggerValue)
            .replace("{name}", elderly.getName()));
        alert.setTriggerValue(triggerValue);
        alert.setNormalRange(normalRange);
        alert.setStatus("ACTIVE");
        alert.setCreatedAt(LocalDateTime.now());
        alert.setObservationId(observation.getId());
        
        healthAlertMapper.insert(alert);
        log.info("为老人{}创建{}级别{}预警", elderly.getName(), rule.getAlertLevel(), alertType);
        
        // 发送健康预警短信通知
        try {
            smsNotificationService.sendHealthAlertNotification(alert);
            log.info("为老人{}发送健康预警短信通知成功", elderly.getName());
        } catch (Exception e) {
            log.error("为老人{}发送健康预警短信通知失败: {}", elderly.getName(), e.getMessage(), e);
        }
        
        // 自动创建干预方案
        try {
            interventionPlanService.createInterventionFromAlert(
                alert.getAlertId(),
                observation.getElderlyId(), 
                elderly.getName(), 
                alertType
            );
            log.info("为预警{}自动创建干预方案成功", alert.getAlertId());
        } catch (Exception e) {
            log.error("为预警{}自动创建干预方案失败: {}", alert.getAlertId(), e.getMessage(), e);
        }
    }
    
    private String generateAlertTitle(String alertType, String alertLevel) {
        String typeText = getAlertTypeText(alertType);
        String levelText = getAlertLevelText(alertLevel);
        return levelText + typeText + "预警";
    }
    
    private String getAlertTypeText(String alertType) {
        switch (alertType) {
            case "TEMPERATURE": return "体温异常";
            case "BLOOD_PRESSURE": return "血压异常";
            case "HEART_RATE": return "心率异常";
            case "BMI": return "体重异常";
            case "SLEEP": return "睡眠异常";
            default: return "健康异常";
        }
    }
    
    private String getAlertLevelText(String alertLevel) {
        switch (alertLevel) {
            case "LOW": return "轻度";
            case "MEDIUM": return "中度";
            case "HIGH": return "重度";
            case "CRITICAL": return "危急";
            default: return "";
        }
    }
    
    private Integer calculateAge(java.time.LocalDate birthDate) {
        if (birthDate == null) return null;
        return Period.between(birthDate, java.time.LocalDate.now()).getYears();
    }
    
    private double calculateBMI(Double height, Double weight) {
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
    
    @Override
    public List<HealthAlert> getAllAlerts() {
        return healthAlertMapper.findAll();
    }
    
    @Override
    public List<HealthAlert> getAlertsByElderlyId(Long elderlyId) {
        return healthAlertMapper.findByElderlyId(elderlyId);
    }
    
    @Override
    public List<HealthAlert> getAlertsByStatus(String status) {
        return healthAlertMapper.findByStatus(status);
    }
    
    @Override
    public List<HealthAlert> getAlertsByLevel(String alertLevel) {
        return healthAlertMapper.findByAlertLevel(alertLevel);
    }
    
    @Override
    public List<HealthAlert> getAlertsByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return healthAlertMapper.findByTimeRange(startTime, endTime);
    }
    
    @Override
    public List<HealthAlert> getAlertsByType(String alertType) {
        return healthAlertMapper.findByAlertType(alertType);
    }
    
    @Override
    @Transactional
    public boolean resolveAlert(Long alertId, String resolvedBy, String resolvedNote) {
        HealthAlert alert = healthAlertMapper.findById(alertId);
        if (alert == null) return false;
        
        alert.setStatus("RESOLVED");
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedNote(resolvedNote);
        alert.setUpdatedAt(LocalDateTime.now());
        
        return healthAlertMapper.updateStatus(alert) > 0;
    }
    
    @Override
    @Transactional
    public boolean ignoreAlert(Long alertId, String resolvedBy, String resolvedNote) {
        HealthAlert alert = healthAlertMapper.findById(alertId);
        if (alert == null) return false;
        
        alert.setStatus("IGNORED");
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedNote(resolvedNote);
        alert.setUpdatedAt(LocalDateTime.now());
        
        return healthAlertMapper.updateStatus(alert) > 0;
    }
    
    @Override
    public Long getActiveAlertCount() {
        return healthAlertMapper.countActive();
    }
    
    @Override
    public Map<String, Object> getAlertStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 总预警数
        statistics.put("totalAlerts", healthAlertMapper.countAll());
        
        // 活跃预警数
        statistics.put("activeAlerts", getActiveAlertCount());
        
        // 按级别统计
        List<Map<String, Object>> levelStats = healthAlertMapper.countByLevel();
        statistics.put("alertsByLevel", levelStats);
        
        // 转换级别统计为前端需要的格式
        // 数据库中的级别：LOW, MEDIUM, HIGH, CRITICAL
        // 前端期望的级别：INFO(提醒), WARNING(警告), CRITICAL(严重)
        int criticalCount = 0;
        int warningCount = 0;
        int infoCount = 0;
        
        for (Map<String, Object> stat : levelStats) {
            String level = (String) stat.get("level");
            Long count = (Long) stat.get("count");
            
            if ("CRITICAL".equals(level)) {
                criticalCount = count.intValue();
            } else if ("HIGH".equals(level)) {
                warningCount = count.intValue();
            } else if ("MEDIUM".equals(level) || "LOW".equals(level)) {
                infoCount += count.intValue();
            }
        }
        
        statistics.put("criticalCount", criticalCount);
        statistics.put("warningCount", warningCount);
        statistics.put("infoCount", infoCount);
        
        // 按类型统计
        List<Map<String, Object>> typeStats = healthAlertMapper.countByType();
        statistics.put("alertsByType", typeStats);
        
        return statistics;
    }
    
    @Override
    public List<HealthAlertRule> getAllRules() {
        return healthAlertRuleMapper.findAll();
    }
    
    @Override
    public List<HealthAlertRule> getEnabledRulesByType(String alertType) {
        return healthAlertRuleMapper.findEnabledByType(alertType);
    }
    
    @Override
    @Transactional
    public boolean addRule(HealthAlertRule rule) {
        rule.setCreatedAt(LocalDateTime.now());
        return healthAlertRuleMapper.insert(rule) > 0;
    }
    
    @Override
    @Transactional
    public boolean updateRule(HealthAlertRule rule) {
        rule.setUpdatedAt(LocalDateTime.now());
        return healthAlertRuleMapper.update(rule) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteRule(Long ruleId) {
        return healthAlertRuleMapper.deleteById(ruleId) > 0;
    }
    
    @Override
    @Transactional
    public boolean toggleRuleStatus(Long ruleId, Boolean enabled) {
        return healthAlertRuleMapper.updateStatus(ruleId, enabled, LocalDateTime.now()) > 0;
    }
    
    @Override
    @Transactional
    public void initializeDefaultRules() {
        // 检查是否已有规则
        if (healthAlertRuleMapper.countAll() > 0) {
            log.info("预警规则已存在，跳过初始化");
            return;
        }
        
        log.info("开始初始化默认预警规则");
        
        // 体温预警规则 - 按照SQL文件的正确配置
        createDefaultRule("体温过低-严重", "TEMPERATURE", "CRITICAL", null, 35.0, "体温严重偏低：{value}°C，正常范围：36.0-37.5°C，请立即就医！");
        createDefaultRule("体温过低-中等", "TEMPERATURE", "MEDIUM", 35.0, 36.0, "体温偏低：{value}°C，正常范围：36.0-37.5°C，请注意保暖");
        createDefaultRule("体温过高-中等", "TEMPERATURE", "MEDIUM", 37.5, 38.5, "体温偏高：{value}°C，正常范围：36.0-37.5°C，请注意休息");
        createDefaultRule("体温过高-严重", "TEMPERATURE", "HIGH", 38.5, 39.5, "体温过高：{value}°C，正常范围：36.0-37.5°C，建议就医");
        createDefaultRule("体温过高-危急", "TEMPERATURE", "CRITICAL", 39.5, null, "体温危急：{value}°C，正常范围：36.0-37.5°C，请立即就医！");
        
        // 血压预警规则 - 只检查收缩压（最高血压）
        createDefaultRule("血压过低-严重", "BLOOD_PRESSURE_SYSTOLIC", "HIGH", null, 90.0, "血压过低：{value}mmHg，正常范围：90-140mmHg，请注意监测");
        createDefaultRule("血压过高-中等", "BLOOD_PRESSURE_SYSTOLIC", "MEDIUM", 140.0, 160.0, "血压偏高：{value}mmHg，正常范围：90-140mmHg，请注意饮食");
        createDefaultRule("血压过高-严重", "BLOOD_PRESSURE_SYSTOLIC", "HIGH", 160.0, 180.0, "血压过高：{value}mmHg，正常范围：90-140mmHg，建议就医");
        createDefaultRule("血压过高-危急", "BLOOD_PRESSURE_SYSTOLIC", "CRITICAL", 180.0, null, "血压危急：{value}mmHg，正常范围：90-140mmHg，请立即就医！");
        
        // 心率预警规则 - 按照SQL文件的正确配置
        createDefaultRule("心率过低-严重", "HEART_RATE", "HIGH", null, 50.0, "心率过低：{value}次/分，正常范围：60-100次/分，建议就医");
        createDefaultRule("心率过低-中等", "HEART_RATE", "MEDIUM", 50.0, 60.0, "心率偏低：{value}次/分，正常范围：60-100次/分，请注意监测");
        createDefaultRule("心率过高-中等", "HEART_RATE", "MEDIUM", 100.0, 120.0, "心率偏高：{value}次/分，正常范围：60-100次/分，请注意休息");
        createDefaultRule("心率过高-严重", "HEART_RATE", "HIGH", 120.0, 150.0, "心率过高：{value}次/分，正常范围：60-100次/分，建议就医");
        createDefaultRule("心率过高-危急", "HEART_RATE", "CRITICAL", 150.0, null, "心率危急：{value}次/分，正常范围：60-100次/分，请立即就医！");
        
        // BMI预警规则 - 按照SQL文件的正确配置
        createDefaultRule("BMI过低-中等", "BMI", "MEDIUM", null, 18.5, "BMI偏低：{value}，正常范围：18.5-24.9，建议增加营养");
        createDefaultRule("BMI过高-中等", "BMI", "MEDIUM", 24.9, 28.0, "BMI偏高：{value}，正常范围：18.5-24.9，建议控制饮食");
        createDefaultRule("BMI过高-严重", "BMI", "HIGH", 28.0, 32.0, "BMI过高：{value}，正常范围：18.5-24.9，建议就医咨询");
        createDefaultRule("BMI过高-危急", "BMI", "CRITICAL", 32.0, null, "BMI严重超标：{value}，正常范围：18.5-24.9，请立即就医！");
        
        // 睡眠预警规则
        createDefaultRule("睡眠不足预警", "SLEEP", "MEDIUM", null, 7.0, "睡眠不足：{value}小时，建议睡眠7-9小时，请改善睡眠质量");
        createDefaultRule("睡眠过多预警", "SLEEP", "LOW", 9.0, null, "睡眠时间过长：{value}小时，建议睡眠7-9小时，请关注身体状况");
        
        log.info("默认预警规则初始化完成");
    }
    
    private void createDefaultRule(String ruleName, String alertType, String alertLevel, 
                                 Double minThreshold, Double maxThreshold, String messageTemplate) {
        HealthAlertRule rule = new HealthAlertRule();
        rule.setRuleName(ruleName);
        rule.setAlertType(alertType);
        rule.setAlertLevel(alertLevel);
        rule.setMinThreshold(minThreshold);
        rule.setMaxThreshold(maxThreshold);
        rule.setMessageTemplate(messageTemplate);
        rule.setEnabled(true);
        rule.setGender("ALL");
        rule.setCreatedAt(LocalDateTime.now());
        rule.setCreatedBy("SYSTEM");
        
        healthAlertRuleMapper.insert(rule);
    }
    
    @Override
    public void checkAndGenerateAlertsByObservationId(Integer observationId) {
        ElderlyObservations observation = elderlyObservationsMapper.selectObservationById(observationId);
        if (observation == null) {
            throw new RuntimeException("观察记录不存在，ID: " + observationId);
        }
        checkAndGenerateAlerts(observation);
    }
    
    @Override
    public int batchCheckAllObservations() {
        List<ElderlyObservations> observations = elderlyObservationsMapper.selectAllObservations();
        int processedCount = 0;
        
        for (ElderlyObservations observation : observations) {
            try {
                checkAndGenerateAlerts(observation);
                processedCount++;
            } catch (Exception e) {
                log.error("处理观察记录失败，ID: {}, 错误: {}", observation.getId(), e.getMessage());
            }
        }
        
        log.info("批量预警检查完成，共处理 {} 条观察记录", processedCount);
        return processedCount;
    }
}