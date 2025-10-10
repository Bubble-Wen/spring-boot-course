package top.jwxiang.springbootfilterinterceptor.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.jwxiang.springbootfilterinterceptor.annotation.RequireRole;
import top.jwxiang.springbootfilterinterceptor.dto.LoginRequest;
import top.jwxiang.springbootfilterinterceptor.dto.LoginResponse;
import top.jwxiang.springbootfilterinterceptor.entity.User;
import top.jwxiang.springbootfilterinterceptor.result.Result;
import top.jwxiang.springbootfilterinterceptor.service.UserService;
import top.jwxiang.springbootfilterinterceptor.utils.JwtUtil;

import java.io.IOException;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 登录接口（无需权限）
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest request,
                                       HttpServletRequest servletRequest) throws IOException {
        /* ===== 1. 打印原始 body ===== */
        String body = new String(servletRequest.getInputStream().readAllBytes());
        log.info("【原始请求体】body = {}", body);

        /* ===== 2. 打印 Content-Type ===== */
        log.info("【Content-Type】= {}", servletRequest.getContentType());

        /* ===== 3. 你原来的业务逻辑 ===== */
        if (request.getUsername() == null || request.getPassword() == null) {
            return Result.error(400, "用户名和密码不能为空");
        }
        User user = userService.authenticate(request);
        if (user == null) {
            return Result.error(400, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());
        LoginResponse response = LoginResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .token(token)
                .build();
        log.info("用户登录成功 - 用户名: {}, 角色: {}", user.getUsername(), user.getRole());
        return Result.ok(response);
    }

    // 查看个人信息（ADMIN和USER角色均可访问）
    @GetMapping("/profile")
    @RequireRole(value = {"ADMIN", "USER"})
    public Result<User> getUserProfile(HttpServletRequest request) {
        // 从Token中获取用户名
        String token = request.getHeader("token");
        String username = jwtUtil.getUsernameFromToken(token);

        // 查询用户信息（隐藏密码）
        User user = userService.getUserByUsername(username);
        if (user != null) {
            user.setPassword(null);
        }

        return Result.ok(user);
    }

    // 查看部门信息（仅ADMIN角色可访问）
    @GetMapping("/department")
    @RequireRole(value = "ADMIN")
    public Result<String> getDepartmentInfo() {
        return Result.ok("部门信息：技术部、产品部、运营部");
    }
}