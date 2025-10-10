package top.jwxiang.springbootfilterinterceptor.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import top.jwxiang.springbootfilterinterceptor.utils.JwtUtil;

@Component
@Slf4j
public class JwtAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        if (token == null || token.isEmpty()){
            response.setStatus(401);
            response.getWriter().write("");
            return false;
        }
        boolean flag = jwtUtil.validateToken(token);
        if (!flag){
            response.setStatus(401);
            response.getWriter().write("");
            return false;
        }
        request.setAttribute("userId", jwtUtil.getUsernameFromToken(token));
        return true;
    }
}