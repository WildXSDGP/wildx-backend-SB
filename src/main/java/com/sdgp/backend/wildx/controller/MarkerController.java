package com.sdgp.backend.wildx.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdgp.backend.wildx.service.MarkerService;

@RestController
@RequestMapping("/markers")
public class MarkerController {
	
	private final MarkerService markerService;
	
	//constructor injection
	public MarkerController(MarkerService markerService) {
		this.markerService=markerService;
	}
	
	

}
