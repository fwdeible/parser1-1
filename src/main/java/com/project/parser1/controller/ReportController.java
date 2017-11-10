package com.project.parser1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.parser1.model.BlockedIP;
import com.project.parser1.model.BlockedIPRepository;


@Controller 
@RequestMapping(path="/report") 
public class ReportController {
	
	@Autowired
	private BlockedIPRepository blockedRepo;
	
	@GetMapping(path="/blocked")
	public @ResponseBody Iterable<BlockedIP> getAllLogEntries() {
		return blockedRepo.findAll();
	}
	

}

