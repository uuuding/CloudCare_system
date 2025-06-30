package com.cloudcare.controller;

import com.cloudcare.common.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 文件上传控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/upload")
@Tag(name = "文件上传", description = "文件上传相关接口")
public class FileUploadController {

    @Value("${cloudcare.file.upload-path}")
    private String uploadPath;

    /**
     * 上传头像
     */
    @PostMapping("/avatar")
    @Operation(summary = "上传头像", description = "上传用户头像文件")
    public Result<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        // 检查文件类型
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            return Result.error("只支持JPG和PNG格式的图片");
        }

        // 检查文件大小（2MB）
        if (file.getSize() > 2 * 1024 * 1024) {
            return Result.error("文件大小不能超过2MB");
        }

        try {
            // 创建上传目录
            String avatarDir = uploadPath + "avatar/";
            File dir = new File(avatarDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 保存文件
            File targetFile = new File(avatarDir + fileName);
            file.transferTo(targetFile);

            // 返回文件URL
            String fileUrl = "/api/files/avatar/" + fileName;
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("fileName", fileName);
            
            log.info("头像上传成功: {}", fileUrl);
            return Result.success(result, "头像上传成功");
            
        } catch (IOException e) {
            log.error("头像上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }

    /**
     * 通用文件上传
     */
    @PostMapping("/file")
    @Operation(summary = "通用文件上传", description = "上传通用文件")
    public Result<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("上传文件不能为空");
        }

        try {
            // 创建上传目录（按日期分类）
            String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String fileDir = uploadPath + "files/" + dateStr + "/";
            File dir = new File(fileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            // 生成文件名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + extension;
            
            // 保存文件
            File targetFile = new File(fileDir + fileName);
            file.transferTo(targetFile);

            // 返回文件URL
            String fileUrl = "/api/files/files/" + dateStr + "/" + fileName;
            Map<String, String> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("fileName", fileName);
            result.put("originalName", originalFilename);
            
            log.info("文件上传成功: {}", fileUrl);
            return Result.success(result, "文件上传成功");
            
        } catch (IOException e) {
            log.error("文件上传失败", e);
            return Result.error("文件上传失败: " + e.getMessage());
        }
    }
}