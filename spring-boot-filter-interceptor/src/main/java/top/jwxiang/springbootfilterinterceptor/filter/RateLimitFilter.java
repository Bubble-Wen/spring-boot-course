package top.jwxiang.springbootfilterinterceptor.filter;

import jakarta.annotation.Resource;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 限流过滤器
 * @author alani
 */

/**
 * 限流过滤器（依赖Redis）
 * 功能作用：限制同一用户（通过userId标识）在指定时间内（60秒）对同一接口的请求次数（最多3次），防止接口被恶意攻击或过度调用
 * 触发条件：
 *   - 对所有请求生效，需从请求头获取userId作为限流标识
 *   - 在请求进入时检查Redis中记录的请求次数，超过阈值则拦截
 * 验证方式：
 *   - 同一userId在60秒内连续调用同一接口（如GET /api/profile）超过3次
 *   - 第4次请求会收到429状态码和"请求过于频繁，请1分钟后再试"的响应
 */
@Slf4j
@Component
public class RateLimitFilter implements Filter {
    // 限流规则：60秒内最多3次请求
    private static final int LIMIT_COUNT = 3;
    private static final int LIMIT_SECONDS = 60;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setContentType("application/json;charset=UTF-8");
        String requestUri = request.getRequestURI();
        // 缓存请求路径，避免重复调用

        try {
            // 1. 获取用户标识（从请求头获取userId）
            String userId = request.getHeader("userId");
            log.info("限流过滤器 - 处理请求，URL: {}, userId: {}", requestUri, userId);

            if (userId == null || userId.isEmpty()) {
                log.warn("限流过滤器 - 拦截请求，原因：userId为空，URL: {}", requestUri);
                response.getWriter().write("{\"code\":400,\"msg\":\"userId不能为空（限流校验）\"}");
                return;
            }

            // 2. 构建Redis限流Key（用户+接口唯一标识）
            String limitKey = "rate_limit:" + userId + ":" + requestUri;
            log.debug("限流过滤器 - Redis Key: {}", limitKey);
            // 调试日志，可选

            // 3. 检查请求次数
            String countStr = stringRedisTemplate.opsForValue().get(limitKey);
            int count = countStr == null ? 0 : Integer.parseInt(countStr);
            log.info("限流过滤器 - 当前请求次数: {}，阈值: {}次/{}秒", count, LIMIT_COUNT, LIMIT_SECONDS);

            // 4. 超过限流阈值：返回429
            if (count >= LIMIT_COUNT) {
                log.warn("限流过滤器 - 触发限流，userId: {}, URL: {}, 次数: {}", userId, requestUri, count);
                response.setStatus(429);
                response.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请1分钟后再试\"}");
                return;
            }

            // 5. 未超过阈值：更新请求次数
            if (count == 0) {
                stringRedisTemplate.opsForValue().set(limitKey, "1", LIMIT_SECONDS, TimeUnit.SECONDS);
                log.info("限流过滤器 - 首次请求，设置过期时间: {}秒，Key: {}", LIMIT_SECONDS, limitKey);
            } else {
                stringRedisTemplate.opsForValue().increment(limitKey);
                log.info("限流过滤器 - 请求次数+1，当前次数: {}", count + 1);
            }

            // 6. 放行请求
            chain.doFilter(request, response);

        } catch (Exception e) {
            // 关键：Redis异常时默认放行（避免因Redis故障导致接口不可用）
            log.error("限流过滤器 - 处理失败（Redis异常），URL: {}", requestUri, e);
            chain.doFilter(request, response);
            // 异常时放行，不影响核心业务
        }
    }
}