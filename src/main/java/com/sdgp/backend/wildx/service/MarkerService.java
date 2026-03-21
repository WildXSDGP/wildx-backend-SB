package com.sdgp.backend.wildx.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sdgp.backend.wildx.model.AnimalMarker;
import com.sdgp.backend.wildx.model.AsianElephantMarker;
import com.sdgp.backend.wildx.model.CrocodileMarker;
import com.sdgp.backend.wildx.model.NationalPark;
import com.sdgp.backend.wildx.model.SlothBearMarker;
import com.sdgp.backend.wildx.model.SpottedDeerMarker;
import com.sdgp.backend.wildx.model.SriLankanLeopardMarker;
import com.sdgp.backend.wildx.model.WaterBuffaloMarker;
import com.sdgp.backend.wildx.repository.AnimalMarkerRepository;
import com.sdgp.backend.wildx.repository.NationalParkRepository;

@Service
public class MarkerService {
	
	private final AnimalMarkerRepository markerRepository;
	private final NationalParkRepository nationalParkRepository;
	
	//constructor injection
	public MarkerService(AnimalMarkerRepository markerRepository,NationalParkRepository nationalParkRepository) {
		this.markerRepository= markerRepository;
		this.nationalParkRepository=nationalParkRepository;
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
   // get recent markers
   public List<AnimalMarker> getRecentMarkers(Long parkId) {
       return markerRepository.findRecentMarkersByParkId(parkId);
   }
   
   // get markers in specific location
   public List<AnimalMarker> getMarkersInBounds(Long parkId, Double minLat, Double maxLat,
           Double minLng, Double maxLng) {
       return markerRepository.findMarkersInBounds(parkId, minLat, maxLat, minLng, maxLng);
   }
   // create marker  method
   public AnimalMarker createMarker(Long parkId, String animalType, Double latitude,
           Double longitude, String reporterName, String notes) {
       NationalPark park = nationalParkRepository.findById(parkId)
               .orElseThrow(() -> new RuntimeException("Park not found with id: " + parkId));

       AnimalMarker marker = createMarkerInstance(animalType, park, latitude, longitude,
               reporterName, notes);

       return markerRepository.save(marker);
   }
   
   // helper method to create marker instance 
   private AnimalMarker createMarkerInstance(String animalType, NationalPark park,
           Double latitude, Double longitude,
           String reporterName, String notes) {
       switch (convertToDiscriminator(animalType)) {
           case "ASIAN_ELEPHANT":
               return new AsianElephantMarker(park, latitude, longitude, reporterName, notes);
           case "SRI_LANKAN_LEOPARD":
               return new SriLankanLeopardMarker(park, latitude, longitude, reporterName, notes);
           case "SPOTTED_DEER":
               return new SpottedDeerMarker(park, latitude, longitude, reporterName, notes);
           case "CROCODILE":
               return new CrocodileMarker(park, latitude, longitude, reporterName, notes);
           case "WATER_BUFFALO":
               return new WaterBuffaloMarker(park, latitude, longitude, reporterName, notes);
           case "SLOTH_BEAR":
               return new SlothBearMarker(park, latitude, longitude, reporterName, notes);
           default:
               throw new IllegalArgumentException("Unknown animal type: " + animalType);
       }

   }
   
   //Create marker with specific spottedAt timestamp
  
  public AnimalMarker createMarkerWithTime(Long parkId, String animalType, Double latitude,
          Double longitude, LocalDateTime spottedAt,
          String reporterName, String notes) {
      NationalPark park = nationalParkRepository.findById(parkId)
              .orElseThrow(() -> new RuntimeException("Park not found with id: " + parkId));

      AnimalMarker marker = createMarkerInstanceWithTime(animalType, park, latitude, longitude,
              spottedAt, reporterName, notes);

      return markerRepository.save(marker);
  }
  
  private AnimalMarker createMarkerInstanceWithTime(String animalType, NationalPark park,
          Double latitude, Double longitude,
          LocalDateTime spottedAt,
          String reporterName, String notes) {
      switch (convertToDiscriminator(animalType)) {
          case "ASIAN_ELEPHANT":
              return new AsianElephantMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          case "SRI_LANKAN_LEOPARD":
              return new SriLankanLeopardMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          case "SPOTTED_DEER":
              return new SpottedDeerMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          case "CROCODILE":
              return new CrocodileMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          case "WATER_BUFFALO":
              return new WaterBuffaloMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          case "SLOTH_BEAR":
              return new SlothBearMarker(park, latitude, longitude, spottedAt, reporterName, notes);
          default:
              throw new IllegalArgumentException("Unknown animal type: " + animalType);
      }
  }
  // update marker 
	  public AnimalMarker updateMarkerNotes(Long markerId, String notes) {
	      AnimalMarker marker = markerRepository.findById(markerId)
	              .orElseThrow(() -> new RuntimeException("Marker not found with id: " + markerId));
	      marker.setNotes(notes);
	      return markerRepository.save(marker);
	  }
  
	  // delete marker
	  public void deleteMarker(Long markerId) {
	        if (!markerRepository.existsById(markerId)) {
	            throw new RuntimeException("Marker not found with id: " + markerId);
	        }
	        markerRepository.deleteById(markerId);
	    }
  



}
