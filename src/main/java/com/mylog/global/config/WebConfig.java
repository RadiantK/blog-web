package com.mylog.global.config;

import com.mylog.global.argumentResolver.LoginMemberArgumentResolver;
import com.mylog.global.interceptor.BlogNameCheckInterceptor;
import com.mylog.global.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/*.ico", "/error",
                        "/", "/user/join/**", "/user/login", "/user/logout", "/user/help/**",
                        "/api/**");

        registry.addInterceptor(new BlogNameCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**", "/img/**", "/js/**", "/*.ico", "/error",
                        "/", "/user/join/**", "/user/login", "/user/logout", "/user/help/**",
                        "/user/mypage/blog", "/api/**");
    }
}
