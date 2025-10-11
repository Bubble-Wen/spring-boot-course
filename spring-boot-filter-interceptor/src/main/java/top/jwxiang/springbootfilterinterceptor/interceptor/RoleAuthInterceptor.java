package top.jwxiang.springbootfilterinterceptor.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import top.jwxiang.springbootfilterinterceptor.annotation.RequireRole;
import top.jwxiang.springbootfilterinterceptor.utils.JwtUtil;

import java.util.Arrays;
import java.util.List;

/**
 * JWT权限拦截器（依赖@RequireRole注解）
 * 功能作用：验证请求的JWT令牌有效性，并检查用户角色是否符合接口要求的访问权限
 * 触发条件：
 *   - 仅对标注@RequireRole注解的控制器方法生效（如/api/profile需要ADMIN/USER角色）
 *   - 在控制器方法执行前（preHandle）验证Token和角色权限
 * 验证方式：
 *   - 无Token访问带@RequireRole的接口：返回401"请先登录"
 *   - 无效/过期Token访问：返回401"Token无效或已过期"
 *   - 角色不匹配（如USER访问ADMIN接口）：返回403"权限不足"
 *   - 有效Token且角色匹配：正常访问接口
 */
@Slf4j
@Component
@AllArgsConstructor
public class RoleAuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 仅拦截控制器方法（非控制器请求直接放行）
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // 2. 检查方法是否需要权限（是否有@RequireRole注解）
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            return true;
            // 无注解：无需权限，直接放行
        }

        // 3. 从请求头获取Token
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请先登录（Token为空）\"}");
            return false;
        }

        // 4. 验证Token有效性（包含过期校验）
        if (!jwtUtil.validateToken(token) || jwtUtil.isTokenExpired(token)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Token无效或已过期\"}");
            return false;
        }

        // 5. 权限校验（用户角色是否匹配）
        String userRole = jwtUtil.getUserRoleFromToken(token);
        List<String> requiredRoles = Arrays.asList(requireRole.value());
        if (!requiredRoles.contains(userRole)) {
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"权限不足（所需角色：" + Arrays.toString(requireRole.value()) + "，当前角色：" + userRole + "）\"}");
            return false;
        }

        // 6. 权限通过：放行
        log.info("JWT认证通过 - 用户名: {}, 角色: {}", jwtUtil.getUsernameFromToken(token), userRole);
        return true;
    }
}