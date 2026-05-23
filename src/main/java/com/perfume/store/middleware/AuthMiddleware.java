package com.perfume.store.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthMiddleware implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession session = request.getSession(false);
        String requestURI = request.getRequestURI();

        // Allow public pages without login
        if (requestURI.startsWith("/auth") ||
                requestURI.startsWith("/css") ||
                requestURI.startsWith("/js") ||
                requestURI.startsWith("/images") ||
                requestURI.equals("/") ||
                requestURI.equals("/products")) {
            return true;
        }

        // Block access if not logged in
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/auth/login");
            return false;
        }

        return true;
    }
}
