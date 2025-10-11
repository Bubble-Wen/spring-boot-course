package top.jwxiang.springbootfilterinterceptor.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 最先执行
public class RequestDebugFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(RequestDebugFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // 1. 先读取 body（无论是否是 login）
        String body = new String(req.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // 2. 如果是 login 接口，打印日志
        if (req.getRequestURI().equals("/api/login") && "POST".equalsIgnoreCase(req.getMethod())) {
            log.info("【RequestDebugFilter】URI={}, Content-Type={}, Body={}",
                    req.getRequestURI(),
                    req.getContentType(),
                    body);
        }

        // 3. 重新包装请求，让后续还能读取 body
        HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(req) {
            private final byte[] bytes = body.getBytes(StandardCharsets.UTF_8);

            @Override
            public ServletInputStream getInputStream() {
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                return new ServletInputStream() {
                    @Override
                    public boolean isFinished() { return bis.available() == 0; }
                    @Override
                    public boolean isReady() { return true; }
                    @Override
                    public void setReadListener(ReadListener listener) {}
                    @Override
                    public int read() { return bis.read(); }
                };
            }
        };

        chain.doFilter(wrapped, response);
    }
}