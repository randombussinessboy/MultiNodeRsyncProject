package com.zhaoyanyang.dfss.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("login")
    public String login(){
        return "/pages/login.html";
    }


    @RequestMapping("index")
    public String index(HttpServletRequest request){
        Boolean flag = (Boolean) request.getSession().getAttribute("flag");
        if(flag !=null &&flag==true) {
            return "/pages/index.html";
        }
        else {
            return "redirect:/login";
        }
    }

}