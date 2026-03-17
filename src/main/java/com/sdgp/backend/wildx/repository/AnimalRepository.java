package com.sdgp.backend.wildx.repository;

import com.sdgp.backend.wildx.model.WildlifeAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Data access layer for Wildlife Animals.
 * Supports advanced search and filtering for the WildX Gallery.
 */
@Repository
public interface AnimalRepository extends JpaRepository<WildlifeAnimal, String> {

    /**
     * A powerful multi-axis filter query. 
     * Handles search terms, category chips, and park selection all in one go.
     * It ignores filters set to 'All' or empty strings to return broader results.
     */
    @Query("""
        SELECT a FROM WildlifeAnimal a 
        WHERE 
          (:search IS NULL OR :search = '' OR 
            LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(a.scientificName) LIKE LOWER(CONCAT('%', :search, '%')) OR 
            LOWER(a.parkLocation) LIKE LOWER(CONCAT('%', :search, '%'))
          )
          AND (:category IS NULL OR :category = 'All' OR a.category = :category)
          AND (:park IS NULL OR :park = 'All' OR a.parkLocation = :park)
        ORDER BY a.name ASC
        """)
    List<WildlifeAnimal> findByFilters(
            @Param("search")   String search,
            @Param("category") String category,
            @Param("park")     String park
    );

    /** * Returns only the animals marked as favorites by the user. 
     */
    List<WildlifeAnimal> findByIsFavoriteTrue();

    /** * Filters animals based on their conservation status (e.g., Endangered). 
     */
    List<WildlifeAnimal> findByStatus(String status);

    /** * Dynamically fetches all unique categories from the database.
     * Used to populate the Filter Chips in the Flutter UI.
     */
    @Query("SELECT DISTINCT a.category FROM WildlifeAnimal a ORDER BY a.category")
    List<String> findDistinctCategories();

    /** * Dynamically fetches all unique park locations.
     * Used to build the Park Filter Bar in the Flutter UI.
     */
    @Query("SELECT DISTINCT a.parkLocation FROM WildlifeAnimal a ORDER BY a.parkLocation")
    List<String> findDistinctParks();
}