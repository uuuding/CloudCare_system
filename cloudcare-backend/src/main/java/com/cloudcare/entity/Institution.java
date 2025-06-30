package com.cloudcare.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 机构信息实体类
 *
 * @author cloudcare
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("institution_info")
@Schema(description = "机构信息")
public class Institution {

    @TableId(value = "institution_id", type = IdType.AUTO)
    @Schema(description = "机构ID")
    private Long institutionId;

    @Schema(description = "机构名称")
    private String name;

    @Schema(description = "机构类型")
    private String type;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "联系人")
    private String contactPerson;

    @Schema(description = "联系电话")
    private String contactPhone;

    @Schema(description = "总床位数")
    private Integer bedTotal;

    @Schema(description = "可用床位数")
    private Integer bedAvailable;

    @Schema(description = "简介")
    private String description;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 计算已入住床位数
     */
    @Schema(description = "已入住床位数")
    public Integer getOccupiedBeds() {
        if (bedTotal == null || bedAvailable == null) {
            return 0;
        }
        return bedTotal - bedAvailable;
    }

    /**
     * 计算入住率
     */
    @Schema(description = "入住率")
    public Double getOccupancyRate() {
        if (bedTotal == null || bedTotal == 0) {
            return 0.0;
        }
        return (double) getOccupiedBeds() / bedTotal * 100;
    }
}