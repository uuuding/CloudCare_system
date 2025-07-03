package com.cloudcare.aspect;

import com.alibaba.fastjson2.JSON;
import com.cloudcare.common.Result;
import com.cloudcare.common.annotation.Log;
import com.cloudcare.common.enums.BusinessType;
import com.cloudcare.entity.SystemLog;
import com.cloudcare.service.SystemLogService;
import com.cloudcare.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 操作日志记录处理切面
 *
 * @author CloudCare
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LogAspect {

    private final SystemLogService systemLogService;

    /**
     * 配置织入点
     */
    @Pointcut("@annotation(com.cloudcare.common.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     * @param result    返回结果
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        handleLog(joinPoint, null, result);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    /**
     * 处理日志记录
     *
     * @param joinPoint 连接点
     * @param e         异常
     * @param result    返回结果
     */
    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object result) {
        try {
            // 获得注解
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }

            // 获取当前的用户
            String username;
            try {
                username = SecurityUtil.getCurrentUsername();
                if (username == null) {
                    username = "anonymous";
                }
            } catch (Exception ex) {
                log.warn("获取当前用户名失败: {}", ex.getMessage());
                username = "anonymous";
            }

            // 构建系统日志对象
            SystemLog systemLog = new SystemLog();
            systemLog.setModule(controllerLog.title());
            systemLog.setBusinessType(controllerLog.businessType().name());
            
            // 获取操作名称，不再使用完整的方法名
            String methodName = joinPoint.getSignature().getName();
            // 根据方法名设置更友好的操作名称
            String operation;
            switch (methodName) {
                case "login":
                    operation = "用户登录";
                    break;
                case "logout":
                    operation = "用户退出";
                    break;
                case "register":
                    operation = "用户注册";
                    break;

                case "addElderlyProfile":
                    operation = "有新的老人档案导入";
                    break;
                case "updateElderlyProfile":
                    operation = "档案信息变更";
                    break;
                case "addObservation":
                    operation = "健康评估有新增";
                    break;
                case "resolveAlert":
                    operation = "健康预警已处理";
                    break;
                case "createAppointment":
                    operation = "有新的预约记录";
                    break;
                case "updateAppointment":
                    operation = "修改预约信息";
                    break;
                case "updateStatus":
                    operation = "更新设备状态";
                    break;
                case "updatePassword":
                    operation = "密码修改";
                    break;
                case "add":
                    operation = "有新的设备信息";
                    break;
                case "updateConfig":
                    operation = "更新系统参数";
                    break;
                default:
                    // 如果没有特殊处理的方法，则使用方法名
                    operation = methodName;
            }
            systemLog.setOperation(operation);
            
            systemLog.setRequestMethod(getRequestMethod());
            systemLog.setUsername(username);
            systemLog.setRequestUrl(getRequestUrl());
            systemLog.setIpAddress(SecurityUtil.getIpAddress());
            systemLog.setCreateTime(LocalDateTime.now());
            systemLog.setResponseStatus(e == null ? 200 : 500); // 200成功 500失败

            // 设置请求参数
            if (controllerLog.isSaveRequestData()) {
                setRequestValue(joinPoint, systemLog);
            }

            // 设置响应参数
            if (controllerLog.isSaveResponseData() && result != null) {
                // 对于认证相关操作，记录简化的内容，避免敏感信息
                if ("USER".equals(controllerLog.title())) {
                    try {
                        if (result instanceof Result) {
                            Result<?> resultObj = (Result<?>) result;
                            if ("login".equals(methodName)) {
                                // 登录操作
                                if (resultObj.isSuccess() && resultObj.getData() instanceof Map) {
                                    @SuppressWarnings("unchecked")
                                    Map<String, Object> data = (Map<String, Object>) resultObj.getData();
                                    String logContent = String.format("登录成功，用户名: %s",
                                        data.get("username"));
                                    systemLog.setContent(logContent);
                                } else {
                                    systemLog.setContent("登录失败");
                                }
                            } else if ("logout".equals(methodName)) {
                                // 注销操作
                                if (resultObj.isSuccess()) {
                                    systemLog.setContent("用户退出登录成功");
                                } else {
                                    systemLog.setContent("用户退出登录失败");
                                }
                            } else if ("register".equals(methodName)) {
                                // 注册操作
                                if (resultObj.isSuccess()) {
                                    // 从请求参数中提取用户名
                                    String registerUsername = "";
                                    try {
                                        // 尝试从请求参数中获取用户名
                                        Object[] args = joinPoint.getArgs();
                                        if (args != null && args.length > 0 && args[0] != null) {
                                            // 假设第一个参数是RegisterDTO
                                            // 使用反射获取username字段
                                            java.lang.reflect.Method getUsernameMethod = args[0].getClass().getMethod("getUsername");
                                            Object usernameObj = getUsernameMethod.invoke(args[0]);
                                            if (usernameObj != null) {
                                                registerUsername = usernameObj.toString();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        log.warn("获取注册用户名失败: {}", ex.getMessage());
                                    }
                                    
                                    if (!registerUsername.isEmpty()) {
                                        systemLog.setContent(String.format("用户 %s 注册成功", registerUsername));
                                    } else {
                                        systemLog.setContent("用户注册成功");
                                    }
                                } else {
                                    systemLog.setContent("用户注册失败");
                                }
                            } else {
                                systemLog.setContent(resultObj.getMessage());
                            }
                        }
                    } catch (Exception ex) {
                        log.warn("处理认证日志内容异常: {}", ex.getMessage());
                        systemLog.setContent(operation + "操作");
                    }
                } else {
                    // 对于其他模块，记录详细的操作结果
                    try {
                        if (result instanceof Result) {
                            Result<?> resultObj = (Result<?>) result;
                            if (resultObj.isSuccess()) {
                                String detailedContent = generateDetailedLogContent(methodName, joinPoint.getArgs(), operation);
                                systemLog.setContent(detailedContent);
                            } else {
                                systemLog.setContent(operation + "失败: " + resultObj.getMessage());
                            }
                        } else {
                            systemLog.setContent(operation + "操作完成");
                        }
                    } catch (Exception ex) {
                        log.warn("处理日志内容异常: {}", ex.getMessage());
                        systemLog.setContent(operation + "操作");
                    }
                }
            }

            // 设置异常信息
            if (e != null) {
                systemLog.setStackTrace(e.getMessage());
                systemLog.setLevel("ERROR");
            } else {
                systemLog.setLevel("INFO");
            }

            // 保存数据库
            systemLogService.saveLog(systemLog);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("记录操作日志异常: {}", exp.getMessage(), exp);
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param joinPoint 连接点
     * @param systemLog 系统日志
     */
    private void setRequestValue(JoinPoint joinPoint, SystemLog systemLog) {
        try {
            String requestMethod = systemLog.getRequestMethod();
            if ("PUT".equals(requestMethod) || "POST".equals(requestMethod)) {
                String params = argsArrayToString(joinPoint.getArgs());
                systemLog.setRequestParams(params);
            }
        } catch (Exception e) {
            log.warn("获取请求参数异常: {}", e.getMessage());
        }
    }

    /**
     * 获取请求方法
     */
    private String getRequestMethod() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getMethod() : "";
    }

    /**
     * 获取请求URL
     */
    private String getRequestUrl() {
        HttpServletRequest request = getRequest();
        return request != null ? request.getRequestURI() : "";
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        StringBuilder params = new StringBuilder();
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (Objects.nonNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o);
                        params.append(jsonObj).append(" ");
                    } catch (Exception e) {
                        log.warn("参数序列化异常: {}", e.getMessage());
                    }
                }
            }
        }
        return params.toString().trim();
    }

    /**
     * 判断是否需要过滤的对象
     *
     * @param o 对象信息
     * @return 如果是需要过滤的对象，则返回true；否则返回false
     */
    private boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(HttpServletRequest.class);
        } else if (clazz.getName().contains("HttpServletRequest") ||
                   clazz.getName().contains("HttpServletResponse") ||
                   clazz.getName().contains("MultipartFile")) {
            return true;
        }
        return false;
    }

    /**
     * 生成详细的日志内容
     *
     * @param methodName 方法名
     * @param args 方法参数
     * @param operation 操作名称
     * @return 详细的日志内容
     */
    private String generateDetailedLogContent(String methodName, Object[] args, String operation) {
        try {
            switch (methodName) {
                case "addObservation":
                    return generateHealthObservationLog(args, operation);
                case "addElderlyProfile":
                case "updateElderlyProfile":
                    return generateElderlyProfileLog(args, operation);
                case "createAppointment":
                    return generateAppointmentLog(args, operation);
                case "add":
                    return generateDeviceLog(args, operation);
                case "resolveAlert":
                    return generateAlertLog(args, operation);
                case "updateStatus":
                    return generateDeviceStatusLog(args, operation);
                default:
                    return operation + "成功";
            }
        } catch (Exception e) {
            log.warn("生成详细日志内容失败: {}", e.getMessage());
            return operation + "成功";
        }
    }

    /**
     * 生成健康评估日志内容
     */
    private String generateHealthObservationLog(Object[] args, String operation) {
        try {
            if (args != null && args.length > 0 && args[0] != null) {
                Object dto = args[0];
                StringBuilder content = new StringBuilder(operation + "记录");
                
                // 使用反射获取健康数据
                try {
                    java.lang.reflect.Method getElderlyIdMethod = dto.getClass().getMethod("getElderlyId");
                    Object elderlyId = getElderlyIdMethod.invoke(dto);
                    if (elderlyId != null) {
                        content.append("，老人ID: ").append(elderlyId);
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getTemperatureMethod = dto.getClass().getMethod("getTemperature");
                    Object temperature = getTemperatureMethod.invoke(dto);
                    if (temperature != null) {
                        content.append("，体温: ").append(temperature).append("℃");
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getSystolicPressureMethod = dto.getClass().getMethod("getSystolicPressure");
                    Object systolicPressure = getSystolicPressureMethod.invoke(dto);
                    if (systolicPressure != null) {
                        content.append("，收缩压: ").append(systolicPressure).append(" mmHg");
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getHeartRateMethod = dto.getClass().getMethod("getHeartRate");
                    Object heartRate = getHeartRateMethod.invoke(dto);
                    if (heartRate != null) {
                        content.append("，心率: ").append(heartRate).append(" bpm");
                    }
                } catch (Exception ignored) {}
                
                return content.toString();
            }
        } catch (Exception e) {
            log.warn("生成健康评估日志失败: {}", e.getMessage());
        }
        return operation + "成功";
    }

    /**
     * 生成老人档案日志内容
     */
    private String generateElderlyProfileLog(Object[] args, String operation) {
        try {
            if (args != null && args.length > 0 && args[0] != null) {
                Object profile = args[0];
                StringBuilder content = new StringBuilder(operation);
                
                try {
                    java.lang.reflect.Method getNameMethod = profile.getClass().getMethod("getName");
                    Object name = getNameMethod.invoke(profile);
                    if (name != null) {
                        content.append("，姓名: ").append(name);
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getGenderMethod = profile.getClass().getMethod("getGender");
                    Object gender = getGenderMethod.invoke(profile);
                    if (gender != null) {
                        content.append("，性别: ").append(gender);
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getAgeMethod = profile.getClass().getMethod("getAge");
                    Object age = getAgeMethod.invoke(profile);
                    if (age != null) {
                        content.append("，年龄: ").append(age).append("岁");
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getPhoneMethod = profile.getClass().getMethod("getPhone");
                    Object phone = getPhoneMethod.invoke(profile);
                    if (phone != null && !phone.toString().isEmpty()) {
                        content.append("，联系电话: ").append(phone);
                    }
                } catch (Exception ignored) {}
                
                return content.toString() + "，请及时调度责任医师";
            }
        } catch (Exception e) {
            log.warn("生成老人档案日志失败: {}", e.getMessage());
        }
        return operation + "，请及时调度责任医师";
    }

    /**
     * 生成预约日志内容
     */
    private String generateAppointmentLog(Object[] args, String operation) {
        try {
            if (args != null && args.length > 0 && args[0] != null) {
                Object appointment = args[0];
                StringBuilder content = new StringBuilder(operation);
                
                try {
                    java.lang.reflect.Method getElderlyIdMethod = appointment.getClass().getMethod("getElderlyId");
                    Object elderlyId = getElderlyIdMethod.invoke(appointment);
                    if (elderlyId != null) {
                        content.append("，老人ID: ").append(elderlyId);
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getAppointmentDateMethod = appointment.getClass().getMethod("getAppointmentDate");
                    Object appointmentDate = getAppointmentDateMethod.invoke(appointment);
                    if (appointmentDate != null) {
                        content.append("，预约时间: ").append(appointmentDate);
                    }
                } catch (Exception ignored) {}
                
                return content.toString() + ",请相关责任医师尽快安排行程";
            }
        } catch (Exception e) {
            log.warn("生成预约日志失败: {}", e.getMessage());
        }
        return operation + ",请相关责任医师尽快安排行程";
    }

    /**
     * 生成设备日志内容
     */
    private String generateDeviceLog(Object[] args, String operation) {
        try {
            if (args != null && args.length > 0 && args[0] != null) {
                Object device = args[0];
                StringBuilder content = new StringBuilder(operation);
                
                try {
                    java.lang.reflect.Method getDeviceNameMethod = device.getClass().getMethod("getDeviceName");
                    Object deviceName = getDeviceNameMethod.invoke(device);
                    if (deviceName != null) {
                        content.append("，设备名称: ").append(deviceName);
                    }
                } catch (Exception ignored) {}
                
                try {
                    java.lang.reflect.Method getDeviceTypeMethod = device.getClass().getMethod("getDeviceType");
                    Object deviceType = getDeviceTypeMethod.invoke(device);
                    if (deviceType != null) {
                        content.append("，设备类型: ").append(deviceType);
                    }
                } catch (Exception ignored) {}
                
                return content.toString();
            }
        } catch (Exception e) {
            log.warn("生成设备日志失败: {}", e.getMessage());
        }
        return operation;
    }

    /**
     * 生成告警日志内容
     */
    private String generateAlertLog(Object[] args, String operation) {
        try {
            if (args != null && args.length > 0) {
                StringBuilder content = new StringBuilder(operation);
                
                // 通常告警处理的第一个参数是告警ID
                if (args[0] != null) {
                    content.append("，预警ID为: ").append(args[0]);
                }
                
                return content.toString();
            }
        } catch (Exception e) {
            log.warn("生成告警日志失败: {}", e.getMessage());
        }
        return operation;
    }

    /**
     * 生成设备状态日志内容
     */
    private String generateDeviceStatusLog(Object[] args, String operation) {
        try {
            if (args != null && args.length >= 2) {
                StringBuilder content = new StringBuilder(operation);
                
                // 通常第一个参数是设备ID，第二个参数是状态
                if (args[0] != null) {
                    content.append("，设备ID: ").append(args[0]);
                }
                if (args[1] != null) {
                    content.append("，新状态: ").append(args[1]);
                }
                
                return content.toString() + "，请联系管理员与厂家核对设备信息";
            }
        } catch (Exception e) {
            log.warn("生成设备状态日志失败: {}", e.getMessage());
        }
        return operation  + "，请联系管理员与厂家核对设备信息";
    }
}