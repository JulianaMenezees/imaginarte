package com.imaginarte.imaginarte_teste.service.autenticador;

import com.imaginarte.imaginarte_teste.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(CookieService.getCookie(request, "usuarioId") != null) {
            return true;
        }
        response.sendRedirect("/login");
        return false;
    }

}
