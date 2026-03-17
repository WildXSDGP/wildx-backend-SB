package com.sdgp.backend.wildx.service;

import com.sdgp.backend.wildx.dto.PhotoDTO;
import com.sdgp.backend.wildx.model.WildlifePhoto;

import java.util.List;

public interface PhotoService {
    List<PhotoDTO> getAllPhotos();
    List<PhotoDTO> getFilteredPhotos(String animalType, String parkName);
    PhotoDTO uploadPhoto(WildlifePhoto photo);
    List<PhotoDTO> getPhotosByUser(String uploadedBy);
    void deletePhoto(String id);
}
