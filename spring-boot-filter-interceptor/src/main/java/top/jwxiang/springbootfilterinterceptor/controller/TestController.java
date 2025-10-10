package top.jwxiang.springbootfilterinterceptor.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.jwxiang.springbootfilterinterceptor.result.Result;


@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @GetMapping("/test/filter")
    public Result<String> testFilter(@RequestParam String name) {
        return Result.ok("Hello, " + name);
    }

    @GetMapping("/pay/{id}")
    public Result<String> pay(@PathVariable long id) {
        log.info("开始支付");
        return Result.ok("支付成功，订单号：" + id);
    }
    @GetMapping("/test/cors")
    public Result<String> testCors() {
        return Result.ok("跨域请求测试通过，当前时间：" + System.currentTimeMillis());
    }

}