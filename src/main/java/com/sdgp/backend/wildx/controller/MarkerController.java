package com.sdgp.backend.wildx.controller;

import java.util.List;
import java.util.Map;

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
	
	// controller to get verified markers 
	@GetMapping("/park/{parkId}/verified")
    public ResponseEntity<List<AnimalMarker>> getVerifiedMarkersByPark(@PathVariable Long parkId) {
        try {
            List<AnimalMarker> markers = markerService.getVerifiedMarkersByPark(parkId);
            return ResponseEntity.ok(markers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	// controller to get unverified markers
	@GetMapping("/unverified")
    public ResponseEntity<List<AnimalMarker>> getUnverifiedMarkers() {
        try {
            List<AnimalMarker> markers = markerService.getUnverifiedMarkers();
            return ResponseEntity.ok(markers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
	
	// get animal types in park
	@GetMapping("/park/{parkId}/animal-types")
    public ResponseEntity<List<String>> getAnimalTypesInPark(@PathVariable Long parkId) {
        try {
            List<String> animalTypes = markerService.getAnimalTypesInPark(parkId);
            return ResponseEntity.ok(animalTypes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //controller to count animal by markers count
     
    @GetMapping("/park/{parkId}/counts")
    public ResponseEntity<Map<String, Long>> getMarkerCountsByAnimalType(@PathVariable Long parkId) {
        try {
            Map<String, Long> counts = markerService.getMarkerCountsByAnimalType(parkId);
            return ResponseEntity.ok(counts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}
