package com.anubhav.bestseller.backend.filters;


import com.anubhav.bestseller.backend.contstants.AuthConstants;
import com.anubhav.bestseller.backend.model.SessionData;
import com.anubhav.bestseller.backend.model.SessionDataHolder;
import com.anubhav.bestseller.backend.service.SessionService;
import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@Order(1)
public class AuthenticationFilter implements Filter {

    @Autowired
    SessionService sessionService;

    private final Set<String> urlsToExclude = new HashSet<>(Arrays.asList("/login", "/login/callback"));

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        // do not check session id for login pages
        if(this.urlsToExclude.contains(httpServletRequest.getRequestURI())){
            chain.doFilter(request, response);
            return;
        }

        // check if the session id exists for the given session data
        SessionData sessionData = this.getSessionData(httpServletRequest);

        // if session data is null, redirect to login page
        if(sessionData == null){
            httpServletResponse.sendRedirect("/login");
            return;
        }

        try{
            // set session data in thread local
            SessionDataHolder.setSessionData(sessionData);
            chain.doFilter(request, response);
        }finally {
            SessionDataHolder.clearSessionData();
        }

    }

    private SessionData getSessionData(HttpServletRequest request){
        // find if session id exists in the request payload
        Cookie[] cookies = request.getCookies();
        if(cookies == null){
            return null;
        }
        String sessionId =  null;
        for(Cookie cookie:  cookies){
            if(cookie.getName().equalsIgnoreCase(AuthConstants.sessionId)){
                sessionId =  cookie.getValue();
            }
        }

        // for the found session id, find the information in backend
        return this.sessionService.getSessionDataBySessionId(sessionId);
    }
}

