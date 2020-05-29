package com.zhaoyanyang.dfss.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding.Use;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhaoyanyang.dfss.pojo.User;
import com.zhaoyanyang.dfss.service.DfssUserService;
import com.zhaoyanyang.dfss.util.FileUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Controller
public class UserController {

	@Resource
	FileUtil fileUtil;
	
	@Autowired DfssUserService dfssUserService;

	// @Value("${spring.user-setting}")
	// private File systemSetting;

	@RequestMapping("getUser")
	@ResponseBody
	public Map<String, Object> getUser() throws IOException {
		Map<String, Object> result = new HashMap<>();

//		ClassPathResource resource = new ClassPathResource("static/pages/user.json");
//		// 有些系统提示找不到资源，可以把上面的代码换成下面这句：
//		// ClassPathResource resource = new ClassPathResource("picture/bottom.png");
//		File sourceFile = resource.getFile();
		
		List<User> allUsers=dfssUserService.getAllUsers();
		
		result.put("rows", allUsers);
		return result;
	}

	@RequestMapping("user/login")
	@ResponseBody
	public Map<String, Object> login(HttpServletRequest request, String name, String pass) throws IOException {
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", "0");
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pass)) {
			result.put("msg", "参数错误");
			return result;
		}
		
//		ClassPathResource resource = new ClassPathResource("static/pages/user.json");
		// 有些系统提示找不到资源，可以把上面的代码换成下面这句：
		// ClassPathResource resource = new ClassPathResource("picture/bottom.png");
//		File sourceFile = resource.getFile();
		result.put("msg", "账号或密码错误");
		
		//这里只是没有数据库的密码验证版本改动到有数据库时的验证版本 为了减少逻辑改动这样写 千万不要全表查询，
		//根据这个用户名去查找账号然后验证密码才是对的
//		List<User> list = JSONObject.parseArray(fileUtil.readString(sourceFile), User.class);
		List<User> list = dfssUserService.getAllUsers();
		for (User user1 : list) {
			if (user1.getName().equals(name) && user1.getPass().equals(pass)) {
				result.put("code", "1");
				result.put("msg", "");
				request.getSession().setAttribute("flag", true);

			}
		}
		return result;
	}

	@RequestMapping("add")
	@ResponseBody
	public Map<String, Object> add(String name, String pass) throws IOException {

		Map<String, Object> result = new HashMap<>();
		result.put("code", "0");
		if (StringUtils.isEmpty(name) || StringUtils.isEmpty(pass)) {
			result.put("msg", "参数错误");
			return result;
		}
//		ClassPathResource resource = new ClassPathResource("static/pages/user.json");
		// 有些系统提示找不到资源，可以把上面的代码换成下面这句：
		// ClassPathResource resource = new ClassPathResource("picture/bottom.png");
//		File sourceFile = resource.getFile();
		
//		List<User> list = JSONObject.parseArray(fileUtil.readString(sourceFile), User.class);
		List<User> list=dfssUserService.getAllUsers();
		boolean flag = true;
		for (User user1 : list) {
			if (user1.getName().equals(name)) {
				result.put("code", "0");
				result.put("msg", "账号已存在");
				flag = false;
				break;
			}
		}
		if (flag) {
//			list.add(new User(name, pass));
//			fileUtil.writeString(sourceFile, JSON.toJSONString(list));
			
			User tmp=new User(name, pass);
			dfssUserService.addUser(tmp);
			
			
			result.put("code", "1");
			result.put("msg", "2");
		}
		return result;
	}
	
	
	@RequestMapping("delete")
	@ResponseBody
	public Map<String, Object> delete(String name) throws IOException {

		Map<String, Object> result = new HashMap<>();
		result.put("code", "0");
		result.put("msg", "账号不存在");
		if (StringUtils.isEmpty(name) ) {
			result.put("msg", "参数错误");
			return result;
		}
//		ClassPathResource resource = new ClassPathResource("static/pages/user.json");
		// 有些系统提示找不到资源，可以把上面的代码换成下面这句：
		// ClassPathResource resource = new ClassPathResource("picture/bottom.png");
//		File sourceFile = resource.getFile();
		
		//List<User> list = JSONObject.parseArray(fileUtil.readString(sourceFile), User.class);
		
		List<User> list = dfssUserService.getAllUsers();
		boolean flag = false;
		
		User target = null;
		
		for (User user1 : list) {
			if (user1.getName().equals(name)) {
				target=user1;
				flag = true;
				break;
			}
		}
		if (flag) {
			
			
//			list.remove(target);
//			fileUtil.writeString(sourceFile, JSON.toJSONString(list));
			dfssUserService.deleteUser(name);
			result.put("code", "1");
			result.put("msg", "删除成功");
		}
		
		return result;
	}

}
