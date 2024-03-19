package com.anubhav.bestseller.backend.model;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SessionDataHolder {

    public static void setSessionData(SessionData sessionData){
        RequestContextHolder.currentRequestAttributes().setAttribute("sessionData", sessionData, RequestAttributes.SCOPE_REQUEST);
    }

    public static SessionData getSessionData(){
        return (SessionData) RequestContextHolder.currentRequestAttributes().getAttribute("sessionData", RequestAttributes.SCOPE_REQUEST);
    }

    public static void clearSessionData(){
        RequestContextHolder.currentRequestAttributes().removeAttribute("sessionData", RequestAttributes.SCOPE_REQUEST);
    }
}
