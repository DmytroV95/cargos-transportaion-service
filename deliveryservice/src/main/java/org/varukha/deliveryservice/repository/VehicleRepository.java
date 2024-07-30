package org.varukha.deliveryservice.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.varukha.deliveryservice.model.Vehicle;

/**
 * Repository interface for accessing Vehicle entities in the database.
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    /**
     * Retrieves a Vehicle entity by its vehicle number along with its associated cargos.
     *
     * @param number The vehicle number of the Vehicle entity to retrieve
     * @return An Optional containing the Vehicle entity with its associated cargos, if found
     */
    @Query("FROM Vehicle v LEFT JOIN FETCH v.cargos WHERE v.vehicleNumber = :number")
    Optional<Vehicle> findByVehicleNumber(String number);

    /**
     * Retrieves all Vehicle entities along with their associated cargos, paginated.
     *
     * @param pageable The pagination information
     * @return A list of Vehicle entities with their associated cargos, paginated
     */
    @Query("FROM Vehicle v LEFT JOIN FETCH v.cargos")
    List<Vehicle> findAllWithCargos(Pageable pageable);
}
