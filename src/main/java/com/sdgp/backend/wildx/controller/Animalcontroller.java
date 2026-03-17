
package com.sdgp.backend.wildx.controller;

import org.springframework.web.bind.annotation.RestController;
import com.wildx.gallery.dto.AnimalDTO;
import com.wildx.gallery.dto.AnimalFilterRequest;
import com.wildx.gallery.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/animals")
@RequiredArgsConstructor
@Tag(name = "Animals", description = "Wildlife animal endpoints for WildX Gallery") 
public class AnimalController {

    private final AnimalService animalService;

    /**
     * Entry point for the Gallery list. 
     * Handles both the initial full load and real-time filtering 
     * as the user types in the search bar or selects chips.
     */
    @GetMapping
    @Operation(summary = "Get all animals or apply filters via query params")
    public ResponseEntity<List<Animaldto>> getAnimals(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String park) {

        // Check if the user is actually using filters; if so, pass to the filter logic
        if ((search != null && !search.isBlank())
                || (category != null && !category.isBlank())
                || (park != null && !park.isBlank())) {

            AnimalFilterRequest filter = new AnimalFilterRequest();
            filter.setSearch(search);
            filter.setCategory(category);
            filter.setPark(park);
            return ResponseEntity.ok(animalService.getFilteredAnimals(filter));
        }

        // Default view: Show everything
        return ResponseEntity.ok(animalService.getAllAnimals());
    }

    /**
     * Provides data for the BottomSheet or Detail view.
     * Triggered when a user taps on an animal card.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get animal by ID — used by AnimalDetailSheet")
    public ResponseEntity<Animaldto> getAnimalById(@PathVariable String id) {
        return ResponseEntity.ok(animalService.getAnimalById(id));
    }

    /**
     * Toggles the heart icon status. 
     * Instead of a separate 'get', it returns the updated object 
     * so Flutter can immediately update the UI state.
     */
    @PatchMapping("/{id}/favorite")
    @Operation(summary = "Toggle favorite — mirrors _toggleFavorite() in WildlifeGalleryScreen")
    public ResponseEntity<Animaldto> toggleFavorite(@PathVariable String id) {
        return ResponseEntity.ok(animalService.toggleFavorite(id));
    }

    /**
     * Dedicated endpoint for the Favorites filter or tab.
     */
    @GetMapping("/favorites")
    @Operation(summary = "Get all favorited animals")
    public ResponseEntity<List<Animaldto>> getFavorites() {
        return ResponseEntity.ok(animalService.getFavorites());
    }

    /**
     * Dynamically populates the Category Filter Chips. 
     * If a new category is added to the DB, it appears in the app automatically.
     */
    @GetMapping("/categories")
    @Operation(summary = "Get distinct categories — feeds CategoryFilterChips")
    public ResponseEntity<List<String>> getCategories() {
        return ResponseEntity.ok(animalService.getCategories());
    }

    /**
     * Populates the Park selection bar (e.g., Yala, Wilpattu).
     */
    @GetMapping("/parks")
    @Operation(summary = "Get distinct park names — feeds ParkFilterBar")
    public ResponseEntity<List<String>> getParks() {
        return ResponseEntity.ok(animalService.getParks());
    }

    /**
     * Useful for showing the "X species found" label in the UI 
     * without having to download the entire list first.
     */
    @GetMapping("/count")
    @Operation(summary = "Get total animal count — mirrors species found label")
    public ResponseEntity<Map<String, Long>> getCount() {
        return ResponseEntity.ok(Map.of("count", (long) animalService.getAllAnimals().size()));
    }
}