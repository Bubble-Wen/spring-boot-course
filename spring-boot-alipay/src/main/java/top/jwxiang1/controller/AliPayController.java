package top.jwxiang1.controller;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jwxiang1.config.AliPayConfig;
import top.jwxiang1.vo.PayVO;

import java.util.Map;
import java.util.UUID;

/**
 * @author alani
 **/
@RestController
@RequestMapping("/alipay")
@AllArgsConstructor
@Slf4j
public class AliPayController {
    private final AliPayConfig aliPayConfig;

    private static final String GATEWAY_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";
    private static final String FORMAT = "JSON";
    private static final String CHARSET = "utf-8";
    private static final String SIGN_TYPE = "RSA2";

    @GetMapping("/pay")
    public void pay(PayVO aliPay, HttpServletResponse httpResponse) throws Exception {
        // 1. 创建支付客户端
        AlipayClient alipayClient = new DefaultAlipayClient(
                GATEWAY_URL,
                aliPayConfig.getAppId(),
                aliPayConfig.getAppPrivateKey(),
                FORMAT,
                CHARSET,
                aliPayConfig.getAlipayPublicKey(),
                SIGN_TYPE
        );
        // 2. 创建支付请求对象
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(aliPayConfig.getNotifyUrl());
        //随机生成订单号
        aliPay.setOutTradeNo(UUID.randomUUID().toString());
        // 业务参数设置（这些参数名是支付宝SDK规定的）
        request.setBizContent("{" +
                "\"out_trade_no\":\"" + aliPay.getOutTradeNo() + "\"," +
                "\"total_amount\":\"" + aliPay.getTotalAmount() + "\"," +
                "\"subject\":\"" + aliPay.getSubject() + "\"," +
                "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 3. 客户端执行请求
        String form = alipayClient.pageExecute(request).getBody();

        // 4. 输出支付表单页面
        httpResponse.setContentType("text/html;charset=" + CHARSET);
        httpResponse.getWriter().write(form);
        httpResponse.getWriter().flush();
        httpResponse.getWriter().close();
    }

    @PostMapping("/notify")
    public String payNotify(HttpServletRequest request) {
        // 判断返回状态trade_status 支付成功是TRADE_SUCCESS
        if ("TRADE_SUCCESS".equals(request.getParameter("trade_status"))) {
            log.info("=========支付宝异步回调========");
            Map<String, String[]> requestParams = request.getParameterMap();
            //输出回调信息
            requestParams.forEach((key, value) -> {
                log.info("{}={}", key, value);
            });
            // 这里可以做支付成功后的业务逻辑，比如订单入库等。
        }
        return "success";
    }
}