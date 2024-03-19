package com.anubhav.bestseller.backend.controller;


import com.anubhav.bestseller.backend.model.SessionData;
import com.anubhav.bestseller.backend.model.SessionDataHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("")
    public String getHomePage(Model model){
        SessionData sessionData = SessionDataHolder.getSessionData();
        model.addAttribute("userId", sessionData.getUserId());
        model.addAttribute("email", sessionData.getEmail());
        model.addAttribute("isEmailVerified", sessionData.getIsEmailVerified());
        model.addAttribute("name", sessionData.getName());
        model.addAttribute("pictureUrl", sessionData.getPictureUrl());
        model.addAttribute("locale", sessionData.getLocale());
        model.addAttribute("familyName", sessionData.getFamilyName());
        model.addAttribute("giveName", sessionData.getGiveName());
        return  "home";
    }
}
