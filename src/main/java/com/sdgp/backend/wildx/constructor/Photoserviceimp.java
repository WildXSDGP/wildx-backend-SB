package com.sdgp.backend.wildx.constructor;

import com.sdgp.backend.wildx.dto.PhotoDTO;
import com.sdgp.backend.wildx.exception.ResourceNotFoundException;
import com.sdgp.backend.wildx.model.WildlifePhoto;
import com.sdgp.backend.wildx.repository.PhotoRepository;
import com.sdgp.backend.wildx.service.PhotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing the community photo gallery.
 * Handles the storage, filtering, and retrieval of wildlife sightings.
 */
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    /**
     * Fetches the entire community feed.
     */
    @Override
    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAll()
                .stream()
                .map(PhotoDTO::from)
                .toList();
    }

    /**
     * Advanced filtering logic for the Photo Grid.
     * It intelligently checks if the user has selected a specific animal or park 
     * and queries the database accordingly.
     */
    @Override
    public List<PhotoDTO> getFilteredPhotos(String animalType, String parkName) {
        List<WildlifePhoto> photos;

        // Boolean checks to handle "All" selection or null values from the UI chips
        boolean hasAnimal = animalType != null && !animalType.isBlank() && !animalType.equals("All");
        boolean hasPark   = parkName   != null && !parkName.isBlank()   && !parkName.equals("All");

        if (hasAnimal && hasPark) {
            photos = photoRepository.findByAnimalTypeAndParkName(animalType, parkName);
        } else if (hasAnimal) {
            photos = photoRepository.findByAnimalType(animalType);
        } else if (hasPark) {
            photos = photoRepository.findByParkName(parkName);
        } else {
            photos = photoRepository.findAll();
        }

        return photos.stream().map(PhotoDTO::from).toList();
    }

    /**
     * Triggered by the SharePhotoButton in Flutter.
     * Automatically sets the current server time before persisting the record.
     */
    @Override
    public PhotoDTO uploadPhoto(WildlifePhoto photo) {
        // Ensuring every upload has an accurate timestamp for sorting the feed
        photo.setUploadedAt(LocalDateTime.now());
        return PhotoDTO.from(photoRepository.save(photo));
    }

    /**
     * Fetches photos shared by a specific user, sorted by the latest first.
     */
    @Override
    public List<PhotoDTO> getPhotosByUser(String uploadedBy) {
        return photoRepository.findByUploadedByOrderByUploadedAtDesc(uploadedBy)
                .stream()
                .map(PhotoDTO::from)
                .toList();
    }

    /**
     * Safely deletes a photo. If the ID is invalid, it triggers our custom 
     * error response handler.
     */
    @Override
    public void deletePhoto(String id) {
        if (!photoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Photo not found with id: " + id);
        }
        photoRepository.deleteById(id);
    }
}