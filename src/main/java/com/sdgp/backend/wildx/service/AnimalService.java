package com.sdgp.backend.wildx.service;

import com.sdgp.backend.wildx.dto.AnimalDTO;
import com.sdgp.backend.wildx.dto.AnimalFilterRequest;

import java.util.List;

public interface AnimalService {
    List<AnimalDTO> getAllAnimals();
    List<AnimalDTO> getFilteredAnimals(AnimalFilterRequest filter);
    AnimalDTO getAnimalById(String id);
    AnimalDTO toggleFavorite(String id);
    List<AnimalDTO> getFavorites();
    List<String> getCategories();
    List<String> getParks();
}
