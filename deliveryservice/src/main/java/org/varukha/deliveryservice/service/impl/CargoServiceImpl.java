package org.varukha.deliveryservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.varukha.deliveryservice.dto.cargo.CargoListResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchResponseDto;
import org.varukha.deliveryservice.dto.emailmessage.EmailMessageDto;
import org.varukha.deliveryservice.exception.EntityNotFoundException;
import org.varukha.deliveryservice.exception.EntitySaveException;
import org.varukha.deliveryservice.mapper.CargoMapper;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.model.Vehicle;
import org.varukha.deliveryservice.repository.CargoRepository;
import org.varukha.deliveryservice.repository.filter.impl.CargoSpecificationBuilder;
import org.varukha.deliveryservice.service.CargoService;
import org.varukha.deliveryservice.service.VehicleService;

/**
 * Service class for managing cargo operations.
 */
@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {
    private static final Logger LOGGER = LogManager.getLogger(CargoServiceImpl.class);

    private final CargoMapper cargoMapper;
    private final CargoRepository cargoRepository;
    private final VehicleService vehicleService;
    private final CargoSpecificationBuilder specificationBuilder;
    private final KafkaProducerService kafkaProducerService;
    private final ObjectMapper objectMapper;

    @Value("${to_email}")
    private String toEmail;

    /**
     * Saves a new cargo entity.
     *
     * @param requestDto The cargo data to save.
     * @return The saved cargo response DTO.
     */
    @Transactional
    @Override
    public CargoResponseDto save(CargoRequestDto requestDto) {
        Cargo cargo = createCargo(requestDto);
        Vehicle vehicle = vehicleService.getVehicleByNumber(requestDto);
        cargo.setVehicle(vehicle);
        try {
            cargoRepository.save(cargo);
            sendMessageToKafka(cargo);
            LOGGER.info("Cargo saving successfully");
            return cargoMapper.toCargoResponseDto(cargo);
        } catch (Exception e) {
            LOGGER.error("Error saving cargo: {}", e.getMessage());
            sendErrorMessageToKafka(cargo, e);
            throw e;
        }
    }

    /**
     * Retrieves a cargo entity by its ID.
     *
     * @param id The ID of the cargo to retrieve.
     * @return The cargo response DTO.
     * @throws EntityNotFoundException if the cargo with the specified ID is not found.
     */
    @Override
    public CargoResponseDto getById(Long id) {
        Cargo cargo = cargoRepository.findByIdWithVehicle(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find cargo by ID: " + id));
        return cargoMapper.toCargoResponseDto(cargo);
    }

    /**
     * Updates an existing cargo entity.
     *
     * @param id         The ID of the cargo to update.
     * @param requestDto The cargo data for the update.
     * @return The updated cargo response DTO.
     */
    @Transactional
    @Override
    public CargoResponseDto update(Long id, CargoRequestDto requestDto) {
        Cargo updatedCargo = cargoMapper.toModel(requestDto);
        Vehicle vehicleByNumber = vehicleService.getVehicleByNumber(requestDto);
        updatedCargo.setId(id);
        updatedCargo.setVehicle(vehicleByNumber);
        Cargo savedCargo = cargoRepository.save(updatedCargo);
        return cargoMapper.toCargoResponseDto(savedCargo);
    }

    /**
     * Deletes a cargo entity by its ID.
     *
     * @param id The ID of the cargo to delete.
     */
    @Override
    public void deleteById(Long id) {
        cargoRepository.deleteById(id);
    }

    /**
     * Retrieves a paginated and filtered list of cargo entities.
     *
     * @param pageable            Pagination information.
     * @param searchParametersDto Parameters for filtering cargo entities.
     * @return The paginated list of cargo DTOs.
     */
    @Override
    @Transactional
    public CargoListResponseDto getPaginatedFilteredList(
            Pageable pageable, CargoSearchRequestDto searchParametersDto) {
        Specification<Cargo> cargoSpecification = specificationBuilder.build(searchParametersDto);
        Page<Cargo> cargoPage = cargoRepository.findAll(cargoSpecification, pageable);
        List<CargoSearchResponseDto> cargoDtoList = cargoPage.getContent()
                .stream()
                .map(cargoMapper::toCargoSearchResponseDto)
                .toList();
        return new CargoListResponseDto(
                cargoDtoList,
                cargoPage.getNumber() + 1,
                cargoPage.getTotalPages());
    }

    /**
     * Retrieves a filtered list of cargo entities.
     *
     * @param searchParametersDto Parameters for filtering cargo entities.
     * @return The filtered list of cargo DTOs.
     */
    @Override
    public List<CargoResponseDto> getFilteredList(CargoSearchRequestDto searchParametersDto) {
        Specification<Cargo> cargoSpecification = specificationBuilder.build(searchParametersDto);
        return cargoRepository.findAll(cargoSpecification)
                .stream()
                .map(cargoMapper::toCargoResponseDto)
                .toList();
    }

    /**
     * Creates a new Cargo entity from the request DTO.
     *
     * @param requestDto The cargo request DTO.
     * @return The created Cargo entity.
     */
    private Cargo createCargo(CargoRequestDto requestDto) {
        Cargo cargo = new Cargo();
        cargo.setDescription(requestDto.description());
        cargo.setWeight(requestDto.weight());
        cargo.setStatus(requestDto.status());
        return cargo;
    }

    /**
     * Sends a message to Kafka about the created cargo.
     *
     * @param cargo The cargo entity.
     */
    private void sendMessageToKafka(Cargo cargo) {
        EmailMessageDto emailMessage = EmailMessageDto.builder()
                .to(toEmail)
                .subject("New Cargo Created")
                .content("Cargo with description: "
                        + cargo.getDescription()
                        + " has been created.")
                .build();
        try {
            String cargoJson = objectMapper.writeValueAsString(emailMessage);
            kafkaProducerService.sendMessage(cargoJson);
        } catch (JsonProcessingException jsonException) {
            LOGGER.error("Error saving cargo: {}",
                    jsonException.getMessage(), jsonException);
            sendErrorMessageToKafka(cargo, jsonException);
            throw new EntitySaveException("Failed to serialize error message", jsonException);
        }
    }

    /**
     * Sends an error message to Kafka about the failed cargo save.
     *
     * @param cargo     The cargo entity.
     * @param exception The exception that occurred.
     */
    private void sendErrorMessageToKafka(Cargo cargo, Exception exception) {
        EmailMessageDto emailMessage = EmailMessageDto.builder()
                .to(toEmail)
                .subject("Cargo Save Error")
                .content("Failed to save cargo with description: "
                        + cargo.getDescription()
                        + ". Error: " + exception.getMessage())
                .build();
        try {
            String errorJson = objectMapper.writeValueAsString(emailMessage);
            kafkaProducerService.sendMessage(errorJson);
        } catch (JsonProcessingException jsonException) {
            LOGGER.error("Error: Serializing error message: {}",
                    jsonException.getMessage());
            throw new EntitySaveException("Failed to serialize error message", jsonException);
        }
    }
}
