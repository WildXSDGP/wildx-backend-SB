package com.sdgp.backend.wildx.repository;

import com.sdgp.backend.wildx.model.WildlifePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for WildlifePhoto entities.
 * Handles database operations for the community sharing gallery.
 */
@Repository
public interface PhotoRepository extends JpaRepository<WildlifePhoto, String> {

    /**
     * Finds photos filtered by both animal type and park name.
     */
    List<WildlifePhoto> findByAnimalTypeAndParkName(String animalType, String parkName);

    /**
     * Finds photos based only on the animal type (e.g., "Leopard").
     */
    List<WildlifePhoto> findByAnimalType(String animalType);

    /**
     * Finds photos based only on the park location (e.g., "Yala").
     */
    List<WildlifePhoto> findByParkName(String parkName);

    /**
     * Retrieves all photos uploaded by a specific user.
     * Results are sorted by 'uploadedAt' in descending order so newest photos appear first.
     */
    List<WildlifePhoto> findByUploadedByOrderByUploadedAtDesc(String uploadedBy);
}