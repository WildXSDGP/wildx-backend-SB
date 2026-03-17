package com.sdgp.backend.wildx.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Database Entity representing a Wildlife species.
 * This maps directly to the MySQL 'wildlife_animal' table and 
 * corresponds to the data structure used in the Flutter mobile app.
 */
@Entity
@Table(name = "wildlife_animal")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WildlifeAnimal {

    @Id
    @Column(nullable = false, unique = true)
    private String id; // Unique ID (matches the one used in the app)

    @NotBlank(message = "Animal name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Scientific name is required")
    @Column(name = "scientific_name", nullable = false)
    private String scientificName;

    /**
     * Category of the animal (e.g., Mammals, Birds, Reptiles).
     * Used for the category filter chips in the gallery.
     */
    @NotBlank
    @Column(nullable = false)
    private String category;

    /**
     * Primary park location where the animal is sighted.
     * Maps to names like Yala, Wilpattu, or Sinharaja.
     */
    @NotBlank
    @Column(name = "park_location", nullable = false)
    private String parkLocation;

    /**
     * Conservation status: Endangered, Vulnerable, or Least Concern.
     * Drives the color-coded badges in the UI.
     */
    @NotBlank
    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String emoji; // Visual representation for the list items

    /**
     * Boolean flag to track if the user has favorited this animal.
     * Managed by the heart icon in the Flutter UI.
     */
    @Column(name = "is_favorite", nullable = false)
    @Builder.Default
    private boolean isFavorite = false;
}