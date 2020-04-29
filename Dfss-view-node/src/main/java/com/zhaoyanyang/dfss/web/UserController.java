package com.zhaoyanyang.dfss.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyanyang.dfss.pojo.User;
import com.zhaoyanyang.dfss.util.FileUtil;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class UserController {

    @Resource
    FileUtil fileUtil;

    @Value("${spring.user-setting}")
    private File systemSetting;

    @RequestMapping("getUser")
    @ResponseBody
    public Map<String,Object> getUser(){
        Map<String,Object> result = new HashMap<>();
        result.put("rows",JSONObject.parseArray(fileUtil.readString(systemSetting),  User.class));
        return  result;
    }


    @RequestMapping("user/login")
    @ResponseBody
    public Map<String,Object> login(HttpServletRequest request,String name, String pass){

        Map<String,Object> result = new HashMap<>();
        result.put("code","0");
        if(StringUtils.isEmpty(name ) || StringUtils.isEmpty(pass )){
            result.put("msg","参数错误");
            return result;
        }
        result.put("msg","账号或密码错误");
        List<User> list = JSONObject.parseArray(fileUtil.readString(systemSetting),  User.class);
        for (User user1 : list) {
            if(user1.getName().equals(name) && user1.getPass().equals(pass)){
                result.put("code","1");
                result.put("msg","");
                request.getSession().setAttribute("flag",true);

            }
        }
        return result;
    }
    @RequestMapping("add")
    @ResponseBody
    public Map<String,Object> add(String name,String pass){

        Map<String,Object> result = new HashMap<>();
        result.put("code","0");
        if(StringUtils.isEmpty(name ) || StringUtils.isEmpty(pass )){
            result.put("msg","参数错误");
            return result;
        }
        List<User> list = JSONObject.parseArray(fileUtil.readString(systemSetting),  User.class);
        boolean flag = true;
        for (User user1 : list) {
            if(user1.getName().equals(name)){
                result.put("code","0");
                result.put("msg","账号已存在");
                flag = false;
                break;
            }
        }
        if(flag){
            list.add(new User(name,pass));
            fileUtil.writeString(systemSetting,JSON.toJSONString(list));
            result.put("code","1");
            result.put("msg","2");
        }
        return result;
    }
 
}
