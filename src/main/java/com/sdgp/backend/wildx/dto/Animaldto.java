package com.sdgp.backend.wildx.dto;

import com.sdgp.backend.wildx.model.WildlifeAnimal;
import lombok.Builder;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for Wildlife Animals.
 * Maps 1:1 with Flutter UI components like AnimalGridCard and AnimalDetailSheet.
 * It also handles color logic so the UI stays consistent with the backend.
 */
@Data
@Builder
public class Animaldto {

    private String id;
    private String name;
    private String scientificName;
    private String category;
    private String parkLocation;
    private String status;
    private String emoji;
    private boolean isFavorite;

    // UI-specific color hints (Hex codes) to keep styling logic centralized
    private String statusColor;    // e.g., "#E53935" for Endangered
    private String statusBgColor;  // e.g., "#FFEBEE" light background

    /**
     * Converts a Database Entity (WildlifeAnimal) into this DTO.
     * This is where we "prepare" the data specifically for the Flutter app.
     */
    public static Animaldto from(WildlifeAnimal animal) {
        return Animaldto.builder()
                .id(animal.getId())
                .name(animal.getName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .parkLocation(animal.getParkLocation())
                .status(animal.getStatus())
                .emoji(animal.getEmoji())
                .isFavorite(animal.isFavorite())
                .statusColor(resolveStatusColor(animal.getStatus()))
                .statusBgColor(resolveStatusBgColor(animal.getStatus()))
                .build();
    }

    /**
     * Determines the text color based on the animal's conservation status.
     * Mirrors the logic previously held in Flutter's gallery_data.dart.
     */
    private static String resolveStatusColor(String status) {
        if (status == null) return "#2ECC71"; // Default Green
        return switch (status) {
            case "Endangered" -> "#E53935"; // Red
            case "Vulnerable" -> "#FF9800"; // Orange
            default           -> "#2ECC71"; // Safe/Stable Green
        };
    }

    /**
     * Determines the badge background color based on the status.
     */
    private static String resolveStatusBgColor(String status) {
        if (status == null) return "#E8F5E9";
        return switch (status) {
            case "Endangered" -> "#FFEBEE";
            case "Vulnerable" -> "#FFF3E0";
            default           -> "#E8F5E9";
        };
    }
}