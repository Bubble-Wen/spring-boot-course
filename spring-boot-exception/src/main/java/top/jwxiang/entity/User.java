package top.jwxiang.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import top.jwxiang.annotation.Phone;

@Data
public class User {
    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @Max(value = 100, message = "年龄不能超过100岁")
    @Max(value = 1,message = "年龄不能小于1岁")
    private int age;

    @NotBlank(message = "手机号不能为空")
    @Phone
    private String phone;
}
