package com.sdgp.backend.wildx.dto;

import com.sdgp.backend.wildx.model.WildlifePhoto;
import lombok.Builder;
import lombok.Data;
import java.time.format.DateTimeFormatter;

/**
 * Data Transfer Object for Wildlife Photos.
 * This is the object that gets sent to the Flutter app to populate the
 * PhotoGrid and User Profile gallery.
 */
@Data
@Builder
public class PhotoDTO {

    private String id;
    private String imageUrl;
    private String animalType;
    private String parkName;
    private String uploadedBy;
    private String uploadedAt; // Formatted as a String for easier parsing in Flutter

    /**
     * Converts a WildlifePhoto Entity into a PhotoDTO.
     * Includes logic to format the timestamp into a human-readable format.
     */
    public static PhotoDTO from(WildlifePhoto photo) {
        // Formatter to convert LocalDateTime to a readable string (e.g., "2026-03-17
        // 14:30")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return PhotoDTO.builder()
                .id(photo.getId())
                .imageUrl(photo.getImageUrl())
                .animalType(photo.getAnimalType())
                .parkName(photo.getParkName())
                .uploadedBy(photo.getUploadedBy())
                .uploadedAt(photo.getUploadedAt() != null ? photo.getUploadedAt().format(formatter) : null)
                .build();
    }
}