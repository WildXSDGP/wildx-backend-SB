package com.sdgp.backend.wildx.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	//Get markers by animal type discriminator
    public List<AnimalMarker> getMarkersByParkAndAnimalType(Long parkId, String animalType) {
        String discriminator = convertToDiscriminator(animalType);
        return markerRepository.findByParkIdAndDiscriminator(parkId, discriminator);
    }
    
    //Helper method to convert animal name to discriminator
    private String convertToDiscriminator(String animalType) {
        return animalType.toUpperCase().replace(" ", "_");
    }
    
    //Get distinct animal types in a park
    public List<String> getAnimalTypesInPark(Long parkId) {
        return markerRepository.findDistinctAnimalTypesByParkId(parkId);
    }
    
    //get verified markers
    public List<AnimalMarker> getVerifiedMarkersByPark(Long parkId) {
        return markerRepository.findByNationalParkIdAndIsVerifiedTrue(parkId);
    }
    
    // get unverified markers
    public List<AnimalMarker> getUnverifiedMarkers() {
        return markerRepository.findByIsVerifiedFalse();
    }
    
    //Get marker count by animal type for a park
   
   public Map<String, Long> getMarkerCountsByAnimalType(Long parkId) {
       List<Object[]> results = markerRepository.countMarkersByAnimalType(parkId);
       Map<String, Long> counts = new HashMap<>();

       for (Object[] result : results) {
           String animalType = (String) result[0];
           Long count = ((Number) result[1]).longValue();
           counts.put(animalType, count);
       }

       return counts;
   }
    


}
