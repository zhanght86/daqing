package com.midas.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private Logger logger = LoggerFactory.getLogger(HomeController.class);
    
    @RequestMapping(value={"/", "/home", "/index"})
    public String home(HttpServletRequest request) {
        logger.info("首页");
        return "main";
    }
    
    
    @RequestMapping(value="/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        logger.info("主页面");
        return "main";
    }
    
    @RequestMapping(value="/logout")
    public String logout() {
        return "login";
    }
    
    @RequestMapping(value="/to")
    public String to(String file) {
        logger.info("去向： {}", file);
        return file;
    }
    
}
