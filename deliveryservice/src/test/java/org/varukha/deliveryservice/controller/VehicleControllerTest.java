package org.varukha.deliveryservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.varukha.deliveryservice.dto.vehicle.VehicleDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleRequestDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleResponseDto;
import org.varukha.deliveryservice.model.enums.VehicleType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VehicleControllerTest {
    protected static MockMvc mockMvc;
    private static final String SQL_SCRIPT_ADD_VEHICLES_DATA_BEFORE_TEST_EXECUTION =
            "classpath:database/vehicles/save_vehicles_to_db.sql";
    private static final String SQL_SCRIPT_ADD_VEHICLE_DATA_BEFORE_TEST_EXECUTION =
            "classpath:database/vehicles/save_vehicle_to_db.sql";
    private static final String SQL_SCRIPT_REMOVE_VEHICLES_DATA_AFTER_TEST_EXECUTION =
            "classpath:database/vehicles/delete_vehicles_from_db.sql";
    private static final String SQL_SCRIPT_REMOVE_VEHICLES_DATA_BEFORE_TEST_EXECUTION =
            "database/vehicles/delete_vehicles_from_db.sql";
    private static final String VEHICLES_ENDPOINT = "/vehicles";
    private static VehicleRequestDto requestDto_vehicle_1;
    private static VehicleResponseDto responseDto_vehicle_1;
    private static VehicleDto vehicleDto_to_update;
    private static List<VehicleResponseDto> responseDtoList;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void setUp(@Autowired WebApplicationContext applicationContext,
                      @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        removeVehicles(dataSource);
    }

    @BeforeEach
    void setUp() {
        requestDto_vehicle_1 = new VehicleRequestDto(
                VehicleType.CAR,
                "ABC1234",
                "Route from 1",
                "Route to 2"
        );
        responseDto_vehicle_1 = new VehicleResponseDto(
                1L,
                requestDto_vehicle_1.type(),
                requestDto_vehicle_1.vehicleNumber(),
                requestDto_vehicle_1.routeFrom(),
                requestDto_vehicle_1.routeTo(),
                Collections.emptyList()
        );
        VehicleResponseDto responseDtoVehicle2 = new VehicleResponseDto(
                1L,
                VehicleType.CAR,
                "ABC123",
                "Route 1",
                "Route 2",
                Collections.emptyList()
        );
        VehicleResponseDto responseDtoVehicle3 = new VehicleResponseDto(
                2L,
                VehicleType.TRUCK,
                "XYZ456",
                "Route 3",
                "Route 4",
                Collections.emptyList()
        );
        VehicleResponseDto responseDtoVehicle4 = new VehicleResponseDto(
                3L,
                VehicleType.SHIP,
                "DEF789",
                "Route 5",
                "Route 6",
                Collections.emptyList()
        );
        responseDtoList = List.of(
                responseDtoVehicle2,
                responseDtoVehicle3,
                responseDtoVehicle4
        );
        vehicleDto_to_update = new VehicleDto(
                1L,
                VehicleType.TRAIN,
                "UPDATED_NUMBER",
                "Updated route 1",
                "Updated route 2"
        );
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        removeVehicles(dataSource);
    }

    @SneakyThrows
    static void removeVehicles(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            SQL_SCRIPT_REMOVE_VEHICLES_DATA_BEFORE_TEST_EXECUTION)
            );
        }
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_REMOVE_VEHICLES_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidRequestDto_ReturnVehicleResponseDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto_vehicle_1);

        MvcResult result = mockMvc.perform(
                        post(VEHICLES_ENDPOINT)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        VehicleResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), VehicleResponseDto.class);

        assertNotNull(actualResponseDto);
        assertNotNull(actualResponseDto.id());
        EqualsBuilder.reflectionEquals(responseDto_vehicle_1, actualResponseDto);
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_VEHICLES_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_VEHICLES_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_ReturnVehicleResponseDto() throws Exception {
        MvcResult result = mockMvc.perform(get(VEHICLES_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<VehicleResponseDto> actualResponseDtoList = objectMapper.readValue(
                result.getResponse().getContentAsString(), objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, VehicleResponseDto.class));

        assertNotNull(actualResponseDtoList);
        assertEquals(3, actualResponseDtoList.size());
        EqualsBuilder.reflectionEquals(responseDtoList.get(0), actualResponseDtoList.get(0));
        EqualsBuilder.reflectionEquals(responseDtoList.get(1), actualResponseDtoList.get(1));
        EqualsBuilder.reflectionEquals(responseDtoList.get(2), actualResponseDtoList.get(2));
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_VEHICLES_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_VEHICLES_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ValidVehicleRequestDto_ReturnVehicleDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto_vehicle_1);
        MvcResult result = mockMvc.perform(
                        put(VEHICLES_ENDPOINT + "/" + vehicleDto_to_update.id())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        VehicleDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), VehicleDto.class);

        assertNotNull(actualResponseDto);
        EqualsBuilder.reflectionEquals(vehicleDto_to_update, actualResponseDto);
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_VEHICLE_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void deleteById_ValidVehicleId_ReturnHttpStatus_204() throws Exception {
        mockMvc.perform(delete(VEHICLES_ENDPOINT + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
