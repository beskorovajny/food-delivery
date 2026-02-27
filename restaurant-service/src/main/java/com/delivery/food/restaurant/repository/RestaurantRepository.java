package com.delivery.food.restaurant.repository;

import com.delivery.food.restaurant.domain.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository interface for {@link Restaurant} entity operations.
 *
 * <p>Provides standard CRUD operations via {@link JpaRepository} and custom query methods
 * for common restaurant-related lookups. All methods are read-only by default unless
 * explicitly annotated with {@code @Modifying} or {@code @Transactional} in the service layer.
 * </p>
 *
 * <p><strong>Production notes:</strong></p>
 * <ul>
 *     <li>Ensure indexes exist on frequently queried fields (name, city, active) — see schema.sql</li>
 *     <li>For complex filtering (e.g. by cuisine, rating, location), consider adding
 *         {@link org.springframework.data.jpa.repository.Query} methods or switching to
 *         {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}</li>
 *     <li>Pagination is mandatory for all list operations to prevent performance issues</li>
 * </ul>
 *
 * @see JpaRepository
 * @see Pageable
 * @see Restaurant
 */

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    /**
     * Finds a restaurant by its exact name.
     *
     * <p>Use this method when exact name matching is required (e.g., during creation to check uniqueness).
     * Note that restaurant names are not guaranteed to be unique in all cases — consider additional
     * business rules in the service layer if needed.
     * </p>
     *
     * @param name the exact name of the restaurant (case-sensitive)
     * @return an {@link Optional} containing the restaurant if found, or empty otherwise
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.name) = LOWER(:name)")
    Optional<Restaurant> findByName(@Param("name") String name);

    /**
     * Retrieves a paginated list of restaurants located in the specified city.
     *
     * <p>This method is useful for location-based restaurant discovery (e.g., "show all in Lviv").
     * Results are sorted according to the provided {@link Pageable} (e.g., by name, rating, etc.).
     * </p>
     *
     * @param city the city name to filter by (case-sensitive)
     * @param pageable pagination and sorting information
     * @return a {@link Page} of restaurants in the given city
     * @apiNote Consider adding a case-insensitive variant or trigram index on city for better search performance
     */
    @Query("SELECT r FROM Restaurant r WHERE LOWER(r.city) = LOWER(:city)")
    Page<Restaurant> findAllByCity(@Param("city") String city, Pageable pageable);

    /**
     * Retrieves a paginated list of restaurants based on their active status.
     *
     * <p>Used to show only currently operating restaurants (active = true) or temporarily closed ones.
     * Results are sorted according to the provided {@link Pageable}.
     * </p>
     *
     * @param isActive {@code true} to get active restaurants, {@code false} for inactive
     * @param pageable pagination and sorting information
     * @return a {@link Page} of restaurants matching the active status
     * @apiNote In production, most queries should filter for active=true by default
     */
    Page<Restaurant> findAllByActive(boolean isActive, Pageable pageable);
}
