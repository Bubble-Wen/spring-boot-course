package top.jwxiang.springbootfilterinterceptor.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;


// 跨域过滤器（全局处理跨域请求）
@Component
public class CorsFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        // 1. 允许的源（生产环境需指定具体域名，如"https://xxx.com"）
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        // 2. 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        // 3. 允许的请求头
        httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, token, userId");
        // 4. 允许携带凭证（如Cookie）
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        // 5. 预检请求缓存时间（秒）：减少OPTIONS请求次数
        httpResponse.setHeader("Access-Control-Max-Age", "3600");

        // 6. 处理预检请求（OPTIONS方法）：直接返回200
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 7. 放行请求
        chain.doFilter(request, response);
    }
}

/**
 * 跨域过滤器
 * 功能作用：处理浏览器跨域请求限制，允许指定域名（如http://localhost:5173）的前端应用访问后端接口
 * 触发条件：
 *   - 对所有请求生效，特别是非简单请求（如带自定义头、PUT/DELETE方法）会先触发OPTIONS预检请求
 *   - 在请求进入过滤器链时设置跨域响应头
 * 验证方式：
 *   - 从http://localhost:5173前端页面发送请求到后端接口（如GET /api/test/cors）
 *   - 检查浏览器控制台无跨域错误，或通过F12查看响应头包含Access-Control-Allow-Origin等跨域相关头
 */