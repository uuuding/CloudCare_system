package com.cloudcare.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件访问控制器
 *
 * @author cloudcare
 */
@Slf4j
@RestController
@RequestMapping("/files")
public class FileController {

    @Value("${cloudcare.file.upload-path}")
    private String uploadPath;

    /**
     * 访问头像文件
     */
    @GetMapping("/avatar/{fileName}")
    public ResponseEntity<Resource> getAvatar(@PathVariable String fileName) {
        try {
            Path filePath = Paths.get(uploadPath + "avatar/" + fileName);
            File file = filePath.toFile();
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            String contentType = Files.probeContentType(filePath);
            
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("获取头像文件失败: {}", fileName, e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 访问通用文件
     */
    @GetMapping("/files/**")
    public ResponseEntity<Resource> getFile(@RequestParam String path) {
        try {
            // 移除开头的 /files/ 前缀
            String relativePath = path.startsWith("/files/") ? path.substring(7) : path;
            Path filePath = Paths.get(uploadPath + "files/" + relativePath);
            File file = filePath.toFile();
            
            if (!file.exists()) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(file);
            String contentType = Files.probeContentType(filePath);
            
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.getName() + "\"")
                    .body(resource);
                    
        } catch (Exception e) {
            log.error("获取文件失败: {}", path, e);
            return ResponseEntity.notFound().build();
        }
    }
}