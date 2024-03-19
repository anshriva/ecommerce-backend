package com.anubhav.bestseller.backend.service;


import com.anubhav.bestseller.backend.contstants.AuthConstants;
import com.anubhav.bestseller.backend.model.SessionData;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.UUID;

@Component
public class SessionService {


    // Initialize the HTTP transport
    NetHttpTransport transport = new NetHttpTransport();

    // Initialize the JSON factory
    JsonFactory jsonFactory = new GsonFactory();

    @Autowired
    SessionCache sessionCache;

    public void addSessionToCookieAndCache(HttpServletResponse httpServletResponse, HttpServletRequest httpServletRequest, SessionData sessionData) {
        Cookie[] cookies = httpServletRequest.getCookies();
        String sessionId = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(AuthConstants.sessionId)) {
                    sessionId = cookie.getValue();
                }
            }
        }

        if(sessionId == null){
            sessionId = UUID.randomUUID().toString();
            Cookie cookie = new Cookie(AuthConstants.sessionId, sessionId);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);
        }

        this.sessionCache.getSessionDataMap().put(sessionId, sessionData);
    }

    public SessionData verifyIdTokenAndGetSessionData(String credential) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(
                        Collections.singletonList(AuthConstants.googleClientId))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();

        GoogleIdToken idToken = verifier.verify(credential);
        if(idToken == null){
            throw new ResponseStatusException( HttpStatus.FORBIDDEN, "invalid id token found");
        }

        return getSessionData(idToken);
    }

    public SessionData getSessionDataBySessionId(String sessionId){
        return this.sessionCache.getSessionDataMap().get(sessionId);
    }

    private static SessionData getSessionData(GoogleIdToken idToken) {
        GoogleIdToken.Payload payload = idToken.getPayload();

        String userId = payload.getSubject();
        String email = payload.getEmail();
        boolean emailVerified = payload.getEmailVerified();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        SessionData sessionData = new SessionData();
        sessionData.setUserId(userId);
        sessionData.setEmail(email);
        sessionData.setIsEmailVerified(emailVerified);
        sessionData.setName(name);
        sessionData.setPictureUrl(pictureUrl);
        sessionData.setLocale(locale);
        sessionData.setFamilyName(familyName);
        sessionData.setGiveName(givenName);
        return sessionData;
    }
}