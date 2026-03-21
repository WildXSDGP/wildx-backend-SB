package com.sdgp.backend.wildx.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdgp.backend.wildx.model.AnimalMarker;
import com.sdgp.backend.wildx.service.MarkerService;

@RestController
@RequestMapping("/markers")
public class MarkerController {
	
	private final MarkerService markerService;
	
	//constructor injection
	public MarkerController(MarkerService markerService) {
		this.markerService=markerService;
	}
	
	//Get All parks 
	@GetMapping("/park/{parkId}")
    public ResponseEntity<List<AnimalMarker>> getMarkersByPark(@PathVariable Long parkId) {
        List<AnimalMarker> markers = markerService.getMarkersByPark(parkId);
        return ResponseEntity.ok(markers);
    }
	
	@GetMapping("/park/{parkId}/type")
    public ResponseEntity<List<AnimalMarker>> getMarkersByParkAndAnimalType(
            @PathVariable Long parkId,
            @RequestParam String animalType) {
        try {
            List<AnimalMarker> markers = markerService.getMarkersByParkAndAnimalType(parkId, animalType);
            return ResponseEntity.ok(markers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
