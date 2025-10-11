package top.jwxiang.schedule.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@TableName("user_account")
public class UserAccount implements Serializable {
    @Serial
    private static final long serialVersionUID = 4335644988265963103L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @ExcelProperty("用户名")
    private String username;

    // 忽略此字段
    @ExcelIgnore
    private String password;

    @ExcelProperty("昵称")
    private String nickname;

    @ExcelProperty("邮箱")
    private String email;

    @ExcelProperty("手机号")
    private String phone;

    @ExcelProperty("头像")
    private String avatarUrl;

    @ExcelProperty("状态")
    private Integer status;

    @TableLogic(value = "0", delval = "1")
    @ExcelProperty("删除状态")
    private Integer deleted;

    @Version
    @ExcelProperty("版本")
    private Integer version;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;
}