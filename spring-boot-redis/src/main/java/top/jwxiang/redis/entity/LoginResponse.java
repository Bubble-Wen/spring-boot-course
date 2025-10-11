package top.jwxiang.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String phone;
    private String message;
    public LoginResponse(String token, String phone) {
        this.token = token;
        this.phone = phone;
        this.message = "登录成功";
    }
}