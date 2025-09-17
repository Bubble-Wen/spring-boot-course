package top.jwxiang.boot.config.controller;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang.boot.config.common.ApiRespopnse;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/hutool/v2")
public class HutoolUtilController {

    /**
     * 1. 日期时间处理接口（JSON请求体）
     * 请求体示例：
     * {
     *   "dateStr": "2024-01-01",
     *   "format": "yyyy-MM-dd"
     * }
     */
    @PostMapping("/date")
    public ApiRespopnse<Map<String, Object>> handleDate(@RequestBody JSONObject params) {
        Map<String, Object> result = new HashMap<>();

        // 获取请求参数（带默认值）
        String dateStr = params.getStr("dateStr", null);
        String format = params.getStr("format", "yyyy-MM-dd HH:mm:ss");

        // 处理逻辑
        result.put("currentTime", DateUtil.format(DateUtil.date(), format));
        if (StrUtil.isNotBlank(dateStr)) {
            long days = DateUtil.between(
                    DateUtil.parse(dateStr),
                    DateUtil.date(),
                    cn.hutool.core.date.DateUnit.DAY
            );
            result.put("daysBetween", days);
        }
        result.put("timestamp", DateUtil.currentSeconds());

        return ApiRespopnse.success("日期处理成功", result);
    }

    /**
     * 2. 加密工具接口（JSON请求体）
     * 请求体示例：
     * {
     *   "content": "123456",
     *   "type": "md5"
     * }
     */
    @PostMapping("/encrypt")
    public ApiRespopnse<Map<String, String>> encrypt(@RequestBody JSONObject params) {
        // 校验必填参数
        String content = params.getStr("content");
        if (StrUtil.isBlank(content)) {
            return ApiRespopnse.error("content参数不能为空");
        }

        Map<String, String> result = new HashMap<>();
        String type = params.getStr("type", "md5");

        if ("md5".equals(type)) {
            result.put("md5", SecureUtil.md5(content));
        } else if ("sha256".equals(type)) {
            result.put("sha256", SecureUtil.sha256(content));
        } else {
            return ApiRespopnse.error("不支持的加密类型，仅支持md5/sha256");
        }

        return ApiRespopnse.success("加密成功", result);
    }

    /**
     * 3. 验证码生成接口（JSON请求体）
     * 请求体示例：
     * {
     *   "width": 120,
     *   "height": 40,
     *   "length": 4
     * }
     */
    @PostMapping("/captcha")
    public ApiRespopnse<Map<String, String>> generateCaptcha(@RequestBody JSONObject params) {
        int width = params.getInt("width", 100);
        int height = params.getInt("height", 40);
        int length = params.getInt("length", 4);

        // 边界校验
        if (width < 50 || height < 30) {
            return ApiRespopnse.error("宽度不能小于50，高度不能小于30");
        }
        if (length < 2 || length > 6) {
            return ApiRespopnse.error("验证码长度必须在2-6之间");
        }

        LineCaptcha captcha = CaptchaUtil.createLineCaptcha(width, height, length, 50);
        Map<String, String> result = new HashMap<>();
        result.put("code", captcha.getCode());
        result.put("imageBase64", captcha.getImageBase64());

        return ApiRespopnse.success("验证码生成成功", result);
    }

    /**
     * 4. HTTP请求工具接口（JSON请求体）
     * 请求体示例：
     * {
     *   "url": "https://httpbin.org/get",
     *   "method": "get",
     *   "params": "{\"name\":\"test\"}"
     * }
     */
    @PostMapping("/http")
    public ApiRespopnse<Map<String, Object>> httpRequest(@RequestBody JSONObject params) {
        String url = params.getStr("url");
        if (StrUtil.isBlank(url)) {
            return ApiRespopnse.error("url参数不能为空");
        }

        Map<String, Object> result = new HashMap<>();
        String method = params.getStr("method", "get");
        String paramsStr = params.getStr("params", "");

        try {
            String response;
            if ("get".equals(method)) {
                response = HttpUtil.get(url);
            } else if ("post".equals(method)) {
                response = HttpUtil.post(url, paramsStr);
            } else {
                return ApiRespopnse.error("不支持的请求方法，仅支持get/post");
            }
            result.put("response", response);
            result.put("status", "success");
        } catch (Exception e) {
            return ApiRespopnse.error("HTTP请求失败：" + e.getMessage());
        }

        return ApiRespopnse.success("HTTP请求成功", result);
    }

    /**
     * 5. 字符串工具接口（JSON请求体）
     * 请求体示例：
     * {
     *   "str": "13800138000",
     *   "operation": "mask"
     * }
     */
    @PostMapping("/string")
    public ApiRespopnse<Map<String, String>> handleString(@RequestBody JSONObject params) {
        String str = params.getStr("str");
        if (StrUtil.isBlank(str)) {
            return ApiRespopnse.error("str参数不能为空");
        }

        Map<String, String> result = new HashMap<>();
        result.put("original", str);
        String operation = params.getStr("operation");

        if ("mask".equals(operation)) {
            result.put("masked", StrUtil.hide(str, 3, 7)); // 从索引3开始隐藏到7（前闭后开）
        } else if ("concat".equals(operation)) {
            result.put("concatenated", StrUtil.concat(true, str, "_suffix"));
        } else if ("empty".equals(operation)) {
            result.put("processed", StrUtil.emptyToDefault(str, "默认值"));
        } else {
            result.put("trimmed", StrUtil.trim(str));
            result.put("length", String.valueOf(StrUtil.length(str)));
        }

        return ApiRespopnse.success("字符串处理成功", result);
    }
}
