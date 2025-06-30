package com.cloudcare.enums;

import lombok.Getter;

/**
 * 机构状态枚举
 */
@Getter
public enum InstitutionStatusEnum {
    
    /**
     * 运营中
     */
    OPERATING("运营中"),
    
    /**
     * 筹备中
     */
    PREPARING("筹备中"),
    
    /**
     * 暂停服务
     */
    SUSPENDED("暂停服务");
    
    private final String value;
    
    InstitutionStatusEnum(String value) {
        this.value = value;
    }
    
    /**
     * 根据状态值获取枚举
     */
    public static InstitutionStatusEnum getByValue(String value) {
        if (value == null) {
            return null;
        }
        
        for (InstitutionStatusEnum status : values()) {
            if (status.getValue().equals(value)) {
                return status;
            }
        }
        
        return null;
    }
    
    /**
     * 验证状态值是否有效
     */
    public static boolean isValidValue(String value) {
        return getByValue(value) != null;
    }
}