package com.sdgp.backend.wildx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sdgp.backend.wildx.model.AnimalMarker;
import com.sdgp.backend.wildx.repository.AnimalMarkerRepository;

@Service
public class MarkerService {
	
	private final AnimalMarkerRepository markerRepository;
	
	//constructor injection
	public MarkerService(AnimalMarkerRepository markerRepository) {
		this.markerRepository= markerRepository;
	}
	
	//Get All Markers from National Park
	public List<AnimalMarker> getMarkersByPark(Long parkId) {
        return markerRepository.findByNationalParkId(parkId);
    }

}
