package com.zhaoyanyang.dfss.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zhaoyanyang.dfss.pojo.Task;

@RestController
public class MasterNodeApartController {

	@GetMapping("/addTask")
	public String addTask() {
		Task newTask=new Task();
		
		
		return "addSuccess";
	}
}
