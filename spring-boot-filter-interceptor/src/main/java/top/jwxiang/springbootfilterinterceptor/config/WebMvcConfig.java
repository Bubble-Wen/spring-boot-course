package top.jwxiang.springbootfilterinterceptor.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.jwxiang.springbootfilterinterceptor.interceptor.BusinessLogInterceptor;
import top.jwxiang.springbootfilterinterceptor.interceptor.ParamValidateInterceptor;
import top.jwxiang.springbootfilterinterceptor.interceptor.RoleAuthInterceptor;
import top.jwxiang.springbootfilterinterceptor.interceptor.TimeStatInterceptor;

@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final TimeStatInterceptor timeStatInterceptor;
    private final BusinessLogInterceptor businessLogInterceptor;
    private final RoleAuthInterceptor roleAuthInterceptor;
    // 变量名同步修改
    private final ParamValidateInterceptor paramValidateInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1. 参数校验拦截器（order=0，最先执行）
        registry.addInterceptor(paramValidateInterceptor)
                .addPathPatterns("/api/**")
                .order(0);

        // 2. 耗时统计拦截器（order=1）
        registry.addInterceptor(timeStatInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static/**", "/error")
                .order(1);

        // 3. JWT权限拦截器（order=2）
        registry.addInterceptor(roleAuthInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/login", "/api/test/filter", "/api/test/cors")
                // 排除无需登录的接口
                .order(2);

        // 4. 业务日志拦截器（order=3）
        registry.addInterceptor(businessLogInterceptor)
                .addPathPatterns("/**") // 拦截所有路径
                .excludePathPatterns("/static/**", "/error")
                .order(3);
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 拦截所有路径
        registry.addMapping("/**")
                // 允许的前端域名和端口
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}