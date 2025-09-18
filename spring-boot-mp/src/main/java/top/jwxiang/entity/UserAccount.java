package top.jwxiang.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xjw
 */
@Data
@Accessors(chain = true)
@TableName("user_account")
public class UserAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 主键ID，⾃增
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * ⽤户名（唯⼀，必填）
     */
    @NotBlank(message = "⽤户名不能为空")
    @Size(max = 50, message = "⽤户名⻓度不能超过50")
    private String username;
    /**
     * 密码（存BCrypt哈希，不存明⽂）
     */
    @NotBlank(message = "密码不能为空")
    @Size(max = 255, message = "密码⻓度过⻓")
    private String password;
    /**
     * 昵称（可空）
     */
    @Size(max = 50, message = "昵称⻓度不能超过50")
    private String nickname;
    /**
     * 邮箱（唯⼀，可空）
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱⻓度不能超过100")
    private String email;
    /**
     * ⼿机号（唯⼀，可空）
     */
    @Size(max = 20, message = "⼿机号⻓度不能超过20")
    private String phone;
    /**
     * 头像URL（可空）
     */
    @Size(max = 255, message = "头像URL⻓度不能超过255")
    private String avatarUrl;
    /**
     * 状态：1-启⽤，0-禁⽤（默认启⽤）
     */
    @TableField("status")
    @Min(value = 0)
    @Max(value = 1)
    private Integer status;
    /**
     * 逻辑删除：1-已删除，0-未删除（默认未删除）
     */
    @TableLogic(value = "0", delval = "1")
    private Integer deleted;
    /**
     * 乐观锁版本号（整型）
     */
    @Version
    private Integer version;
    /**
     * 创建时间（插⼊⾃动填充），时间格式化，东⼋区
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
    /**
     * 更新时间（插⼊与更新⾃动填充）
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updateTime;
}