package com.example.InkHub_backend.controller;

import com.example.InkHub_backend.common.R;
import com.example.InkHub_backend.dto.LoginDTO;
import com.example.InkHub_backend.dto.RegisterDTO;
import com.example.InkHub_backend.service.UserService;
import com.example.InkHub_backend.vo.LoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "认证接口", description = "注册、登录，登录成功返回 JWT token")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @Operation(summary = "用户注册", description = "注册成功即普通用户（role=1），管理员需后续在数据库改 role=2")
    @PostMapping("/register")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }

    @Operation(summary = "用户登录", description = "返回 token 与用户信息；后续请求在 Authorization 头带 Bearer token")
    @PostMapping("/login")
    public R<LoginVO> login(@Valid @RequestBody LoginDTO dto) {
        return R.ok(userService.login(dto));
    }
}