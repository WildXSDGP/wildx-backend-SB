package com.sdgp.backend.wildx.controller;

import com.sdgp.backend.wildx.dto.PhotoDTO;
import com.sdgp.backend.wildx.model.WildlifePhoto;
import com.sdgp.backend.wildx.service.PhotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API for Community Photo Feed and Sharing features.
 * * This controller handles the backend logic for the PhotoGrid 
 * and SharePhotoButton widgets in the WildX Flutter app.
 */
@RestController
@RequestMapping("/api/v1/photos")
@RequiredArgsConstructor
@Tag(name = "Photos", description = "Wildlife community photo endpoints")
public class PhotoController {

    private final PhotoService photoService;

    /**
     * Retrieves the community photo feed.
     * Supports filtering by animal type or park name (e.g., Wilpattu, Yala).
     */
    @GetMapping
    @Operation(summary = "Get photos — optionally filter by animalType and/or parkName")
    public ResponseEntity<List<PhotoDTO>> getPhotos(
            @RequestParam(required = false) String animalType,
            @RequestParam(required = false) String parkName) {
        // Fetches filtered or all photos based on query parameters
        return ResponseEntity.ok(photoService.getFilteredPhotos(animalType, parkName));
    }

    /**
     * Fetches photos uploaded by a specific user.
     * Useful for the "My Contributions" section in the user profile.
     */
    @GetMapping("/user/{uploadedBy}")
    @Operation(summary = "Get photos uploaded by a specific user")
    public ResponseEntity<List<PhotoDTO>> getPhotosByUser(
            @PathVariable String uploadedBy) {
        return ResponseEntity.ok(photoService.getPhotosByUser(uploadedBy));
    }

    /**
     * Saves a new wildlife photo sighting shared by a user.
     * Maps to the SharePhotoButton.onTap() event in Flutter.
     */
    @PostMapping
    @Operation(summary = "Upload a new photo — triggered by SharePhotoButton")
    public ResponseEntity<PhotoDTO> uploadPhoto(
            @Valid @RequestBody WildlifePhoto photo) {
        // Creates a new resource and returns 201 Created status
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(photoService.uploadPhoto(photo));
    }

    /**
     * Deletes a specific photo by its unique ID.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a photo")
    public ResponseEntity<Void> deletePhoto(@PathVariable String id) {
        photoService.deletePhoto(id);
        // Returns 204 No Content to confirm successful deletion
        return ResponseEntity.noContent().build();
    }
}