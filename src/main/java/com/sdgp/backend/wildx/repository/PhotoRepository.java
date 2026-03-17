package com.sdgp.backend.wildx.repository;

import com.sdgp.backend.wildx.model.WildlifePhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhotoRepository extends JpaRepository<WildlifePhoto, String> {

    List<WildlifePhoto> findByAnimalTypeAndParkName(String animalType, String parkName);

    List<WildlifePhoto> findByAnimalType(String animalType);

    List<WildlifePhoto> findByParkName(String parkName);

    List<WildlifePhoto> findByUploadedByOrderByUploadedAtDesc(String uploadedBy);
}
