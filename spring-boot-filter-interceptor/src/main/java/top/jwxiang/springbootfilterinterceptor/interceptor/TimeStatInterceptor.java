package top.jwxiang.springbootfilterinterceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 接口耗时统计拦截器
 * @author alani
 */
@Slf4j
@Component
public class TimeStatInterceptor implements HandlerInterceptor {

    // 存放请求开始时间的属性名
    private static final String START_TIME = "startTime";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        // 放行请求
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 控制器执行后调用（此时还未返回响应）
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 响应返回前调用：计算耗时
        long startTime = (long) request.getAttribute(START_TIME);
        long endTime = System.currentTimeMillis();
        long cost = endTime - startTime;
        // 打印日志（handler包含控制器方法信息）
        log.info("接口耗时 - 方法: {}, 耗时: {}ms", handler.getClass().getSimpleName(), cost);
    }


    /**
     * 耗时统计拦截器
     * 功能作用：精确统计控制器方法的执行耗时，辅助分析接口性能瓶颈
     * 触发条件：
     *   - 拦截所有路径（配置中addPathPatterns("/**")），排除静态资源和错误页
     *   - preHandle在控制器方法执行前触发（记录开始时间）
     *   - afterCompletion在响应返回客户端后触发（计算并记录耗时）
     * 验证方式：
     *   - 调用任意接口（如POST /api/login）
     *   - 查看日志中"接口耗时 - 方法: ... 耗时: ...ms"的输出，耗时为控制器处理时间
     */
}