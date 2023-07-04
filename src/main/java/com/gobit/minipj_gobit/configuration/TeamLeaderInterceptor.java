package com.gobit.minipj_gobit.configuration;

import com.gobit.minipj_gobit.entity.User;
import com.gobit.minipj_gobit.repository.TestRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@RequiredArgsConstructor
public class TeamLeaderInterceptor implements HandlerInterceptor {
    private final TestRepository testRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // AJAX 요청 여부 확인
        String requestedWithHeader = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWithHeader);

        // 세션 확인
        HttpSession httpSession = request.getSession();
        // 세션이 없으면 로그인 페이지로 리다이렉트
        if (httpSession.getAttribute("user") != null) {
            return true;
        } else {
            if (isAjax) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized 상태 코드 반환
                return false;
            } else {
                response.sendRedirect("/login");
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        HttpSession httpSession = request.getSession();
        if (modelAndView != null) {
            User user = (User) httpSession.getAttribute("user");
            modelAndView.addObject("hidtext", testRepository.findByCheckCnt(user.getUSERNUM()));
        }
    }
}