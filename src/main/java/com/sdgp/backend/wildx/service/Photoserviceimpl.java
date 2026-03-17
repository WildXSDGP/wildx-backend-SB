package com.sdgp.backend.wildx.service;

import com.sdgp.backend.wildx.dto.PhotoDTO;
import com.sdgp.backend.wildx.exception.ResourceNotFoundException;
import com.sdgp.backend.wildx.model.WildlifePhoto;
import com.sdgp.backend.wildx.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service implementation for managing community wildlife photos.
 * Handles photo uploads from the 'Share' feature and manages the photo feed filters.
 */
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;

    /**
     * Fetches all photos available in the community gallery.
     */
    @Override
    public List<PhotoDTO> getAllPhotos() {
        return photoRepository.findAll()
                .stream()
                .map(PhotoDTO::from)
                .toList();
    }

    /**
     * Applies dynamic filtering based on user selection in the Flutter app.
     * Checks if "All" is selected to determine whether to apply specific criteria.
     */
    @Override
    public List<PhotoDTO> getFilteredPhotos(String animalType, String parkName) {
        List<WildlifePhoto> photos;

        // Logic to check if actual filter values are provided by the UI chips
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
     * Handles new photo submissions from the SharePhotoButton.
     * Automatically captures the server-side timestamp for accurate feed sorting.
     */
    @Override
    public PhotoDTO uploadPhoto(WildlifePhoto photo) {
        // Ensuring a reliable timestamp instead of relying on the mobile device's clock
        photo.setUploadedAt(LocalDateTime.now());
        return PhotoDTO.from(photoRepository.save(photo));
    }

    /**
     * Retrieves a list of photos uploaded by a specific user, sorted by the newest first.
     */
    @Override
    public List<PhotoDTO> getPhotosByUser(String uploadedBy) {
        return photoRepository.findByUploadedByOrderByUploadedAtDesc(uploadedBy)
                .stream()
                .map(PhotoDTO::from)
                .toList();
    }

    /**
     * Removes a photo from the gallery. Throws an error if the ID is missing,
     * which is caught by the GlobalExceptionHandler.
     */
    @Override
    public void deletePhoto(String id) {
        if (!photoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Photo not found with id: " + id);
        }
        photoRepository.deleteById(id);
    }
}