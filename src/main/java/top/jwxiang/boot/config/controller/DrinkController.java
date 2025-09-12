package top.jwxiang.boot.config.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.boot.config.enums.DrinkStatus;

@RestController
@RequestMapping("/drinks")
public class DrinkController {
    @GetMapping("/{status1}")
    //@PathVariable路径变量
    //@RequestParam请求参数
    //@RequestBody请求体
    //GetMapping 查询
    //PostMapping 新增
    //PutMapping 修改
    //DeleteMapping 删除
    public String getDrinkInfo(@PathVariable DrinkStatus status1) {
        return "您选择的饮料是:" + status1.getType() + "，价格是:" + status1.getPrice();
    }
}
