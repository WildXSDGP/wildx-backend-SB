package com.sdgp.backend.wildx.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Database Entity for community-shared photos.
 * Maps to the 'wildlife_photo' table and syncs with the 
 * Flutter 'WildlifePhoto' model for the community gallery.
 */
@Entity
@Table(name = "wildlife_photo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WildlifePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id; // Automatically generates a unique 36-character string

    @NotBlank(message = "Image URL is mandatory")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    /**
     * Type of animal captured in the photo (e.g., Elephant, Leopard).
     * This helps the Flutter 'PhotoGridTile' categorize the view.
     */
    @NotBlank
    @Column(name = "animal_type", nullable = false)
    private String animalType;

    /**
     * The national park where the photo was taken.
     */
    @NotBlank
    @Column(name = "park_name", nullable = false)
    private String parkName;

    /**
     * The username or ID of the person who shared the sighting.
     */
    @NotBlank
    @Column(name = "uploaded_by", nullable = false)
    private String uploadedBy;

    /**
     * Stores the exact date and time of the upload.
     * Used for sorting the community feed (Newest First).
     */
    @Column(name = "uploaded_at", nullable = false)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();
}