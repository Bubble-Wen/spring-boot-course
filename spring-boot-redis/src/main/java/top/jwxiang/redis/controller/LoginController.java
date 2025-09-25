package top.jwxiang.redis.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.redis.entity.LoginRequest;
import top.jwxiang.redis.entity.LoginResponse;
import top.jwxiang.redis.result.Result;
import top.jwxiang.redis.service.LoginService;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class LoginController {
    private final LoginService loginService;
    @PostMapping("/login")
    public Result<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = loginService.login(loginRequest);
        return Result.ok(loginResponse);
    }
}