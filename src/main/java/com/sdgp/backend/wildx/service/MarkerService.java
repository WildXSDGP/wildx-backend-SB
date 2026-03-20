package com.sdgp.backend.wildx.service;

import org.springframework.stereotype.Service;

import com.sdgp.backend.wildx.repository.AnimalMarkerRepository;

@Service
public class MarkerService {
	
	private final AnimalMarkerRepository markerRepository;
	
	//constructor injection
	public MarkerService(AnimalMarkerRepository markerRepository) {
		this.markerRepository= markerRepository;
	}

}
