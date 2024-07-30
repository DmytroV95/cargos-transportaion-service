package org.varukha.deliveryservice.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.varukha.deliveryservice.model.Cargo;

/**
 * Repository interface for accessing Cargo entities in the database.
 */
public interface CargoRepository extends JpaRepository<Cargo, Long>,
        JpaSpecificationExecutor<Cargo> {

    /**
     * Retrieves a Cargo entity by its ID along with the associated Vehicle entity.
     *
     * @param id The ID of the Cargo entity to retrieve
     * @return An Optional containing the Cargo entity with the associated Vehicle entity, if found
     */
    @Query("FROM Cargo c LEFT JOIN FETCH c.vehicle WHERE c.id = :id")
    Optional<Cargo> findByIdWithVehicle(Long id);
}
