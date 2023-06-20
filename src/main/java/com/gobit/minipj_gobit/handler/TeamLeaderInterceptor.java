package com.gobit.minipj_gobit.handler;

import com.gobit.minipj_gobit.Entity.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

public class TeamLeaderInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView)  {
        //모든 페이지에서 실행
        HttpSession httpSession = request.getSession();
        if (httpSession.getAttribute("user") != null) {
            User user = (User) httpSession.getAttribute("user");
            if (modelAndView != null) {
                modelAndView.addObject("user", user);
            }
        }
    }
}