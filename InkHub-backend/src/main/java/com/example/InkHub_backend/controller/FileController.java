package com.example.InkHub_backend.controller;

import com.example.InkHub_backend.common.BusinessException;
import com.example.InkHub_backend.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

// 文件上传：封面图 / 头像 / 编辑器图片都走这个接口，返回可访问的 URL
@Tag(name = "文件上传接口", description = "图片上传，返回 URL（需登录）")
@RestController
@RequestMapping("/api/upload")
public class FileController {

    // 上传目录（来自 yaml 的 upload.dir）
    @Value("${upload.dir}")
    private String uploadDir;

    // 允许的图片类型
    private static final Set<String> ALLOWED = Set.of("jpg", "jpeg", "png", "gif", "webp");

    @Operation(summary = "上传图片", description = "multipart 表单，字段名 file；返回 /uploads/xxx.jpg 可直接访问")
    @PostMapping
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException(400, "文件不能为空");
        }
        String ext = getExt(file.getOriginalFilename());
        if (!ALLOWED.contains(ext)) {
            throw new BusinessException(400, "仅支持 jpg/png/gif/webp 图片");
        }
        // UUID 重命名：防重名、防路径穿越
        String filename = UUID.randomUUID().toString().replace("-", "") + "." + ext;
        try {
            // 转绝对路径再存：相对路径会被 Tomcat 拼到临时目录导致保存失败
            File dir = new File(uploadDir).getAbsoluteFile();
            if (!dir.exists() && !dir.mkdirs()) {
                throw new BusinessException(500, "上传目录创建失败");
            }
            file.transferTo(new File(dir, filename));
        } catch (IOException e) {
            throw new BusinessException(500, "文件保存失败");
        }
        // 返回给前端的 URL（走静态资源映射，见 1.3）
        return R.ok("/uploads/" + filename);
    }

    private String getExt(String name) {
        if (name == null || !name.contains(".")) {
            throw new BusinessException(400, "文件格式不正确");
        }
        return name.substring(name.lastIndexOf('.') + 1).toLowerCase();
    }
}
