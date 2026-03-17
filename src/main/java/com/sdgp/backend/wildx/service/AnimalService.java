package com.sdgp.backend.wildx.service;

import com.sdgp.backend.wildx.dto.Animaldto;
import com.sdgp.backend.wildx.dto.AnimalFilterRequest;

import java.util.List;

public interface AnimalService {
    List<Animaldto> getAllAnimals();
    List<Animaldto> getFilteredAnimals(AnimalFilterRequest filter);
    Animaldto getAnimalById(String id);
    Animaldto toggleFavorite(String id);
    List<Animaldto> getFavorites();
    List<String> getCategories();
    List<String> getParks();
}
