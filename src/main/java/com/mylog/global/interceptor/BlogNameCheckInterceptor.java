package com.mylog.global.interceptor;

import com.mylog.global.common.Constant;
import com.mylog.member.dto.MemberLoginResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BlogNameCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        MemberLoginResponse member = (MemberLoginResponse) session.getAttribute(Constant.LOGIN_MEMBER);
        if (session != null && member != null) {
            String blogName = member.getBlogName();

            if (StringUtils.hasText(blogName) == false) {
                response.sendRedirect("/mylog.com/user/mypage/blog?error=blog");
                return false;
            }
        }
        return true;
    }
}
