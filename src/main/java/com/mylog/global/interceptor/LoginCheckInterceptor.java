package com.mylog.global.interceptor;

import com.mylog.global.common.Constant;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // /mylog.com 제거
        String uri = request.getRequestURI().substring(10);

        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(Constant.LOGIN_MEMBER) == null) {
            // 로그인으로 redirect
            response.sendRedirect("/mylog.com/user/login?url=" + uri);
            return false;
        }

        return true;
    }
}
