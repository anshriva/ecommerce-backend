package com.anubhav.bestseller.backend.service;


import com.anubhav.bestseller.backend.model.SessionData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class SessionCache {

    // HashMap to store the session id to session data mapping. Later we can add redis instead of a hashmap
    private Map<String, SessionData> sessionDataMap;

    public SessionCache() {
        this.sessionDataMap= new HashMap<>();
    }

    public Map<String, SessionData> getSessionDataMap() {
        return sessionDataMap;
    }

    public void setSessionDataMap(Map<String, SessionData> sessionDataMap) {
        this.sessionDataMap = sessionDataMap;
    }
}