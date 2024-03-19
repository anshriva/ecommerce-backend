package com.anubhav.bestseller.backend.controller;

import com.anubhav.bestseller.backend.model.SessionData;
import com.anubhav.bestseller.backend.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/login")
public class Login {
    @Autowired
    SessionService sessionService;

    @GetMapping("")
    public String login(HttpServletRequest httpServletRequest, Model model){
        String baseUrl = ServletUriComponentsBuilder.fromRequestUri(httpServletRequest)
                .replacePath(null)
                .build()
                .toUriString();
        model.addAttribute("callBackUrl", baseUrl + "/login/callback");
        return "login-ui";
    }

    @PostMapping("/callback")
    public String htmlCallback(
            @RequestParam String credential,
            @RequestParam String g_csrf_token,
            @CookieValue(value = "g_csrf_token", defaultValue = "default") String csrfToken,
            HttpServletResponse httpServletResponse,
            HttpServletRequest httpServletRequest)
            throws IOException, GeneralSecurityException {

        this.validateCookie(g_csrf_token, csrfToken);
        SessionData sessionData =  this.sessionService.verifyIdTokenAndGetSessionData(credential);
        this.sessionService.addSessionToCookieAndCache(httpServletResponse, httpServletRequest, sessionData);
        return "redirect:/";
    }

    private void validateCookie(String csrfTokenFromPayload, String csrfTokenFromCookie) throws AccessDeniedException {
        if(csrfTokenFromCookie == null || csrfTokenFromCookie.trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in cookie is empty");
        }

        if(csrfTokenFromPayload  == null || csrfTokenFromPayload.trim().isEmpty()){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in param is empty");
        }

        if(!csrfTokenFromPayload.equalsIgnoreCase(csrfTokenFromCookie)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "CSRF token in body is not equal to CSRF token in payload");
        }
    }
}