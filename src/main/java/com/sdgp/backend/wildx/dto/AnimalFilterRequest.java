package com.sdgp.backend.wildx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO to capture filtering parameters from the Flutter UI.
 * This class bundles search queries, categories, and park names 
 * into a single object for the AnimalController.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnimalFilterRequest {

    /**
     * The free-text search string from the GallerySearchBar.
     * Matches against animal name, scientific name, or park location.
     */
    private String search;

    /**
     * Selected category from the CategoryFilterChips (e.g., Mammals, Birds).
     * Defaults to 'All' in the UI.
     */
    private String category;

    /**
     * Selected park name from the ParkFilterBar (e.g., Yala, Wilpattu).
     */
    private String park;
}