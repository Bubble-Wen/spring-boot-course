package top.jwxiang.springbootfilterinterceptor.filter;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Enumeration;

@Slf4j
@Component
public class LogFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("LogFilter 初始化...");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 1. 预处理：转换为HttpServletRequest（获取更多信息）
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String url = httpRequest.getRequestURL().toString();// 请求URL
        String method = httpRequest.getMethod(); // 请求方法
        String ip = httpRequest.getRemoteAddr();// 客户端IP

        // 打印请求参数
        StringBuilder params = new StringBuilder();
        Enumeration<String> paramNames = httpRequest.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();
            params.append(name).append("=").append(httpRequest.getParameter(name)).append("&");
        }

        String paramStr = !params.isEmpty() ? params.substring(0, params.length() - 1) : "";

        log.info("请求开始 - URL: {}, 方法: {}, IP: {}, 参数: {}", url, method, ip, paramStr);

        // 2. 放行请求（必须调用，否则请求会被拦截）
        long startTime = System.currentTimeMillis();
        // 执行后续过滤器或控制器
        chain.doFilter(request, response);
        long endTime = System.currentTimeMillis();

        // 3. 后处理：记录耗时
        log.info("请求结束 - URL: {}, 耗时: {}ms", url, endTime - startTime);
    }

    @Override
    public void destroy() {
        log.info("LogFilter 销毁...");
    }
    /**
     * 日志过滤器
     * 功能作用：记录请求的详细信息（URL、请wxs2qaz求方法、IP地址、请求参数）及请求处理耗时，用于请求跟踪和性能初步分析
     * 触发条件：
     *   - 对所有经过该过滤器的请求生效（需在web.xml或配置类中注册并配置拦截路径）
     *   - 在请求进入过滤器链时触发pre处理，在请求处理完成后触发post处理
     * 验证方式：
     *   - 发送任意请求（如GET /api/test/filter?name=test）
     *   - 查看日志输出，应包含"请求开始 - URL: ..."和"请求结束 - URL: ... 耗时: ...ms"的日志记录
     */
}