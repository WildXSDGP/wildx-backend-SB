package com.sdgp.backend.wildx.constructor;

import com.sdgp.backend.wildx.dto.AnimalDTO;
import com.sdgp.backend.wildx.dto.AnimalFilterRequest;
import com.sdgp.backend.wildx.exception.ResourceNotFoundException;
import com.sdgp.backend.wildx.model.WildlifeAnimal;
import com.sdgp.backend.wildx.repository.AnimalRepository;
import com.sdgp.backend.wildx.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

/**
 * Implementation of the AnimalService. 
 * This class handles the actual logic behind searching, filtering, 
 * and managing the favorite status of wildlife species.
 */
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    /**
     * Fetches every animal record and converts them into DTOs for the UI.
     */
    @Override
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(AnimalDTO::from)
                .toList();
    }

    /**
     * Processes complex filtering based on search queries, categories, and park locations.
     * This directly supports the filter chips and search bar in the Flutter app.
     */
    @Override
    public List<AnimalDTO> getFilteredAnimals(AnimalFilterRequest filter) {
        return animalRepository.findByFilters(
                        filter.getSearch(),
                        filter.getCategory(),
                        filter.getPark())
                .stream()
                .map(AnimalDTO::from)
                .toList();
    }

    /**
     * Finds a single animal by ID. Throws a custom exception if the ID is invalid,
     * which is then handled by our GlobalExceptionHandler.
     */
    @Override
    public AnimalDTO getAnimalById(String id) {
        WildlifeAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Animal not found with id: " + id));
        return AnimalDTO.from(animal);
    }

    /**
     * Toggles the favorite status (Like/Unlike).
     * @Transactional ensures that the database update is atomic and safe.
     */
    @Override
    @Transactional
    public AnimalDTO toggleFavorite(String id) {
        WildlifeAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Animal not found with id: " + id));

        // Creating a new instance with the toggled favorite status
        WildlifeAnimal updated = WildlifeAnimal.builder()
                .id(animal.getId())
                .name(animal.getName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .parkLocation(animal.getParkLocation())
                .status(animal.getStatus())
                .emoji(animal.getEmoji())
                .isFavorite(!animal.isFavorite()) // The actual toggle logic
                .build();

        return AnimalDTO.from(animalRepository.save(updated));
    }

    /**
     * Returns only the animals that the user has marked as favorites.
     */
    @Override
    public List<AnimalDTO> getFavorites() {
        return animalRepository.findByIsFavoriteTrue()
                .stream()
                .map(AnimalDTO::from)
                .toList();
    }

    /**
     * Fetches distinct categories and adds an "All" option at the start.
     * This ensures the Flutter FilterChips always have a default state.
     */
    @Override
    public List<String> getCategories() {
        List<String> categories = animalRepository.findDistinctCategories();
        List<String> result = new ArrayList<>();
        result.add("All");
        result.addAll(categories);
        return result;
    }

    /**
     * Fetches distinct parks and prepends the "All" option for the UI selection bar.
     */
    @Override
    public List<String> getParks() {
        List<String> parks = animalRepository.findDistinctParks();
        List<String> result = new ArrayList<>();
        result.add("All");
        result.addAll(parks);
        return result;
    }
}