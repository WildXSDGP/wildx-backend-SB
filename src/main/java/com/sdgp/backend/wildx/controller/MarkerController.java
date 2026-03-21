package com.sdgp.backend.wildx.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
    // controller to get recent markers
    @GetMapping("/park/{parkId}/recent")
    public ResponseEntity<List<AnimalMarker>> getRecentMarkers(@PathVariable Long parkId) {
        try {
            List<AnimalMarker> markers = markerService.getRecentMarkers(parkId);
            return ResponseEntity.ok(markers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // get markers for specific bound
    @GetMapping("/park/{parkId}/bounds")
    public ResponseEntity<List<AnimalMarker>> getMarkersInBounds(
            @PathVariable Long parkId,
            @RequestParam Double minLat,
            @RequestParam Double maxLat,
            @RequestParam Double minLng,
            @RequestParam Double maxLng) {
        try {
            List<AnimalMarker> markers = markerService.getMarkersInBounds(
                    parkId, minLat, maxLat, minLng, maxLng);
            return ResponseEntity.ok(markers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    //controllers to get markers by Id
    @GetMapping("/{markerId}")
    public ResponseEntity<AnimalMarker> getMarkerById(@PathVariable Long markerId) {
        try {
            AnimalMarker marker = markerService.getMarkerById(markerId);
            return ResponseEntity.ok(marker);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    
    @PostMapping
    public ResponseEntity<AnimalMarker> addMarker(@RequestBody MarkerRequest request) {
        try {
            AnimalMarker marker;

            if (request.getSpottedAt() != null) {
                marker = markerService.createMarkerWithTime(
                        request.getParkId(),
                        request.getAnimalType(),
                        request.getLatitude(),
                        request.getLongitude(),
                        request.getSpottedAt(),
                        request.getReporterName(),
                        request.getNotes());
            } else {
                marker = markerService.createMarker(
                        request.getParkId(),
                        request.getAnimalType(),
                        request.getLatitude(),
                        request.getLongitude(),
                        request.getReporterName(),
                        request.getNotes());
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(marker);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}


class MarkerRequest {
    private Long parkId;
    private String animalType;
    private Double latitude;
    private Double longitude;
    private LocalDateTime spottedAt;
    private String reporterName;
    private String notes;

    // Getters and Setters
    public Long getParkId() {
        return parkId;
    }

    public void setParkId(Long parkId) {
        this.parkId = parkId;
    }

    public String getAnimalType() {
        return animalType;
    }

    public void setAnimalType(String animalType) {
        this.animalType = animalType;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getSpottedAt() {
        return spottedAt;
    }

    public void setSpottedAt(LocalDateTime spottedAt) {
        this.spottedAt = spottedAt;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
