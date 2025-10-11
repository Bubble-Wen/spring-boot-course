package top.jwxiang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.jwxiang.entity.UserAccount;

import java.util.List;

public interface UserAccountService extends IService<UserAccount> {
    /** 创建单个⽤户（⾃动加密密码） */
    boolean createUser(UserAccount user);
    /** 批量创建⽤户（⾃动加密密码） */
    boolean createUsers(List<UserAccount> users);
}