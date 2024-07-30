package org.varukha.deliveryservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.varukha.deliveryservice.dto.cargo.CargoListResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoRequestDto;
import org.varukha.deliveryservice.dto.cargo.CargoResponseDto;
import org.varukha.deliveryservice.dto.cargo.CargoSearchResponseDto;
import org.varukha.deliveryservice.dto.vehicle.VehicleInfoDto;
import org.varukha.deliveryservice.model.Cargo;
import org.varukha.deliveryservice.model.Vehicle;
import org.varukha.deliveryservice.model.enums.DeliveryStatus;
import org.varukha.deliveryservice.model.enums.VehicleType;
import org.varukha.deliveryservice.service.impl.KafkaProducerService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CargoControllerTest {
    protected static MockMvc mockMvc;
    private static final String SQL_SCRIPT_ADD_CARGOS_DATA_BEFORE_TEST_EXECUTION =
            "classpath:database/cargos/save_cargos_to_db.sql";
    private static final String SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION =
            "classpath:database/cargos/delete_cargos_from_db.sql";
    private static final String SQL_SCRIPT_DELETE_ALL_DATA_FROM_DB_AFTER_TEST_EXECUTION =
            "database/delete_all_data_from_db.sql";
    private static final String SQL_SCRIPT_ADD_CARGOS_AND_VEHICLES_TO_DB_BEFORE_TEST_EXECUTION =
            "classpath:database/cargos/save_cargos_to_db_without_id.sql";
    private static final String SQL_SCRIPT_ADD_VEHICLES_DATA_BEFORE_TEST_EXECUTION =
            "database/vehicles/save_vehicles_to_db.sql";
    private static final String CARGOS_ENDPOINT = "/cargos";

    private static CargoRequestDto requestDto_cargo_1;
    private static CargoResponseDto responseDto_cargo_1;
    private static Cargo test_cargo_1;
    private static Cargo test_cargo_2;
    private static CargoRequestDto requestDto_cargo_1_to_update;
    private static CargoSearchResponseDto searchResponseDto_1;
    private static CargoSearchResponseDto searchResponseDto_2;
    private static CargoSearchResponseDto searchResponseDto_3;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @BeforeEach
    void setUp() {
        Vehicle testVehicle1 = new Vehicle();
        testVehicle1.setId(1L);
        testVehicle1.setVehicleNumber("ABC123");
        testVehicle1.setType(VehicleType.CAR);
        testVehicle1.setRouteFrom("Route 1");
        testVehicle1.setRouteTo("Route 2");
        testVehicle1.setCargos(Collections.emptyList());

        Vehicle testVehicle2 = new Vehicle();
        testVehicle2.setId(2L);
        testVehicle2.setVehicleNumber("XYZ456");
        testVehicle2.setType(VehicleType.TRUCK);
        testVehicle2.setRouteFrom("Route 3");
        testVehicle2.setRouteTo("Route 4");
        testVehicle2.setCargos(Collections.emptyList());

        requestDto_cargo_1 = new CargoRequestDto(
                "ABC123",
                "Some description",
                10.5,
                DeliveryStatus.PENDING
        );
        responseDto_cargo_1 = new CargoResponseDto(
                1L,
                testVehicle1,
                requestDto_cargo_1.description(),
                requestDto_cargo_1.weight(),
                requestDto_cargo_1.status().getStatusName()
        );
        requestDto_cargo_1_to_update = new CargoRequestDto(
                testVehicle2.getVehicleNumber(),
                "Updated test_cargo 1",
                10.5,
                DeliveryStatus.RETURNED);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setVehicleNumber("ABC123");
        test_cargo_1 = new Cargo();
        test_cargo_1.setId(1L);
        test_cargo_1.setVehicle(vehicle);
        test_cargo_1.setDescription(requestDto_cargo_1.description());
        test_cargo_1.setWeight(requestDto_cargo_1.weight());
        test_cargo_1.setStatus(requestDto_cargo_1.status());

        test_cargo_2 = new Cargo();
        test_cargo_2.setId(2L);
        test_cargo_2.setVehicle(testVehicle1);
        test_cargo_2.setDescription("Cargo 2 description");
        test_cargo_2.setWeight(15.2);
        test_cargo_2.setStatus(DeliveryStatus.DELIVERED);

        searchResponseDto_1 = new CargoSearchResponseDto(
                1L,
                new VehicleInfoDto("CAR", "AB123"),
                "Some description",
                10.5,
                "PENDING"
        );
        searchResponseDto_2 = new CargoSearchResponseDto(
                2L,
                new VehicleInfoDto("TRUCK", "CA355"),
                "Cargo 2 description",
                15.2,
                "DELIVERED"
        );
        searchResponseDto_3 = new CargoSearchResponseDto(
                3L,
                new VehicleInfoDto("TRUCK", "AC587"),
                "Cargo 3 description",
                20.0,
                "DELIVERED"
        );

    }

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext,
                          @Autowired DataSource dataSource) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
        clearDatabase(dataSource);
        addVehicles(dataSource);
    }

    @SneakyThrows
    static void addVehicles(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            SQL_SCRIPT_ADD_VEHICLES_DATA_BEFORE_TEST_EXECUTION)
            );
        }
    }

    @SneakyThrows
    static void clearDatabase(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            SQL_SCRIPT_DELETE_ALL_DATA_FROM_DB_AFTER_TEST_EXECUTION)
            );
        }
    }

    @AfterAll
    static void afterAll(@Autowired DataSource dataSource) {
        clearDatabase(dataSource);
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ValidCargoRequestDto_ReturnCargoResponseDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto_cargo_1);
        MvcResult result = mockMvc.perform(
                        post(CARGOS_ENDPOINT)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        CargoResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CargoResponseDto.class);

        assertNotNull(actualResponseDto);
        assertNotNull(actualResponseDto.id());
        EqualsBuilder.reflectionEquals(responseDto_cargo_1, actualResponseDto);
        verify(kafkaProducerService).sendMessage(any(String.class));
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_CARGOS_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getById_ValidCargoId_ReturnCargoResponseDto() throws Exception {
        MvcResult result = mockMvc
                .perform(
                        get(CARGOS_ENDPOINT + "/" + test_cargo_1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        CargoResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CargoResponseDto.class);

        assertNotNull(actualResponseDto);
        assertNotNull(actualResponseDto.id());
        assertEquals(test_cargo_1.getId(), actualResponseDto.id());
        EqualsBuilder.reflectionEquals(test_cargo_1, actualResponseDto);
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_CARGOS_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ValidCargoRequestDto_ReturnCargoResponseDto() throws Exception {
        String jsonRequest = objectMapper.writeValueAsString(requestDto_cargo_1_to_update);
        MvcResult result = mockMvc.perform(
                        put(CARGOS_ENDPOINT + "/" + test_cargo_1.getId())
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CargoResponseDto actualResponseDto = objectMapper.readValue(
                result.getResponse().getContentAsString(), CargoResponseDto.class);

        assertNotNull(actualResponseDto);
        EqualsBuilder.reflectionEquals(requestDto_cargo_1_to_update, actualResponseDto);
    }

    @Test
    @DisplayName("Test the 'deleteById' endpoint with a valid book ID")
    @Sql(scripts = SQL_SCRIPT_ADD_CARGOS_DATA_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void deleteByIdValidCargoId_ReturnHttpResponse_204() throws Exception {
        mockMvc.perform(delete(CARGOS_ENDPOINT + "/" + test_cargo_2.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @Sql(scripts = SQL_SCRIPT_ADD_CARGOS_AND_VEHICLES_TO_DB_BEFORE_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = SQL_SCRIPT_REMOVE_CARGOS_DATA_AFTER_TEST_EXECUTION,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getCargoListTest_ValidCargoSearchRequestDto_getDataProcessingJson() throws Exception {
        List<CargoSearchResponseDto> cargoSearchDtoList = List.of(
                searchResponseDto_1,
                searchResponseDto_2,
                searchResponseDto_3);
        CargoListResponseDto expectedResult = new CargoListResponseDto(
                cargoSearchDtoList, 1, 1);
        MvcResult result = mockMvc.perform(
                        get(CARGOS_ENDPOINT + "/_list")
                                .contentType(MediaType.APPLICATION_JSON)
                                .param("status", "DELIVERED")
                                .param("type", "TRUCK")
                )
                .andExpect(status().isOk())
                .andReturn();

        CargoListResponseDto actualResult = objectMapper.readValue(
                result.getResponse().getContentAsString(), CargoListResponseDto.class);

        assertNotNull(actualResult);
        assertEquals(2, actualResult.list().size());
        EqualsBuilder.reflectionEquals(expectedResult.list().get(0), actualResult.list().get(0));
        EqualsBuilder.reflectionEquals(expectedResult.list().get(1), actualResult.list().get(1));
    }

    @Test
    void uploadJsonFile_MultipartFile_UploadDataFromJsonToDb() throws Exception {
        Path resourcePath = Path.of("src", "test", "java", "org", "varukha",
                "deliveryservice", "test_upload_file", "test_cargo_data.json");
        byte[] fileContent = Files.readAllBytes(resourcePath);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test_cargo_data.json",
                "application/json",
                fileContent
        );
        mockMvc.perform(multipart(CARGOS_ENDPOINT + "/file/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.successfulImports").value(96))
                .andExpect(jsonPath("$.failedImports").value(4));
    }

    @Test
    void generateExcelReport_MultipartFile_UploadedXmlReport() throws Exception {
        mockMvc.perform(get(CARGOS_ENDPOINT + "/_report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("status", "DELIVERED")
                        .param("type", "TRUCK"))
                .andExpect(status().isOk());
    }
}
