package com.sdgp.backend.wildx.service;

import com.sdgp.backend.wildx.dto.Animaldto;
import com.sdgp.backend.wildx.dto.AnimalFilterRequest;
import com.sdgp.backend.wildx.exception.ResourceNotFoundException;
import com.sdgp.backend.wildx.model.WildlifeAnimal;
import com.sdgp.backend.wildx.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;

/**
 * Service to handle business logic for Wildlife Animals.
 * Orchestrates data between the Repository and the Controller.
 */
@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;

    @Override
    public List<AnimalDTO> getAllAnimals() {
        return animalRepository.findAll()
                .stream()
                .map(AnimalDTO::from)
                .toList();
    }

    @Override
    public List<Animaldto> getFilteredAnimals(AnimalFilterRequest filter) {
        // Uses a custom JPQL query to filter by name, category, and park simultaneously
        return animalRepository.findByFilters(
                        filter.getSearch(),
                        filter.getCategory(),
                        filter.getPark())
                .stream()
                .map(Animaldto::from)
                .toList();
    }

    @Override
    public Animaldto getAnimalById(String id) {
        WildlifeAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Animal not found with id: " + id));
        return Animaldto.from(animal);
    }

    /**
     * Toggles the favorite status and persists it to the database.
     * @Transactional ensures the save operation is safe.
     */
    @Override
    @Transactional
    public Animaldto toggleFavorite(String id) {
        WildlifeAnimal animal = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Animal not found with id: " + id));

        // Rebuilds the object with the toggled favorite state
        WildlifeAnimal updated = WildlifeAnimal.builder()
                .id(animal.getId())
                .name(animal.getName())
                .scientificName(animal.getScientificName())
                .category(animal.getCategory())
                .parkLocation(animal.getParkLocation())
                .status(animal.getStatus())
                .emoji(animal.getEmoji())
                .isFavorite(!animal.isFavorite()) // Toggle
                .build();

        return Animaldto.from(animalRepository.save(updated));
    }

    @Override
    public List<Animaldto> getFavorites() {
        return animalRepository.findByIsFavoriteTrue()
                .stream()
                .map(Animaldto::from)
                .toList();
    }

    @Override
    public List<String> getCategories() {
        // Ensures "All" is the first option for Flutter chips
        List<String> categories = animalRepository.findDistinctCategories();
        List<String> result = new ArrayList<>();
        result.add("All");
        result.addAll(categories);
        return result;
    }

    @Override
    public List<String> getParks() {
        // Ensures "All" is the first option for Flutter park filters
        List<String> parks = animalRepository.findDistinctParks();
        List<String> result = new ArrayList<>();
        result.add("All");
        result.addAll(parks);
        return result;
    }
}