package com.example.driver.integration;

import com.example.driver.dto.CarCreateDto;
import com.example.driver.dto.CarResponseDto;
import com.example.driver.dto.CarStandaloneCreateDto;
import com.example.driver.dto.ContainerDriverResponse;
import com.example.driver.dto.DriverCreateDto;
import com.example.driver.dto.DriverResponse;
import com.example.driver.dto.DriverUpdateDto;
import com.example.driver.dto.RatingCreateDto;
import com.example.driver.model.enums.Gender;
import com.example.driver.repository.CarRepository;
import com.example.driver.repository.DriverRepository;
import com.example.driver.repository.RatingRepository;
import com.example.driver.service.DriverService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;
import java.util.Objects;

import static com.example.driver.util.UnitTestUtils.getDriverCreateDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DriverServiceIntegrationTest {

    private static final String DRIVER_CONTROLLER_URL = "/api/v1/drivers";

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    TestRestTemplate testRestTemplate;

    private Long driverId;

    private Long carId;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3");
    @Autowired
    private DriverService driverService;

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @Test
    void connectionEstablished() {
        assertThat(postgreSQLContainer.isCreated()).isTrue();
        assertThat(postgreSQLContainer.isRunning()).isTrue();
    }

    @BeforeEach
    void setUp() {
        DriverCreateDto driverCreateDto = DriverCreateDto.builder()
                .username("test")
                .email("test@gmail.com")
                .fullName("Full Name")
                .phoneNumber("+375331119912")
                .gender("MALE")
                .carCreateDtoList(List.of(CarCreateDto.builder()
                        .brand("volvo")
                        .color("red")
                        .registrationNumber("0001AM7")
                        .build()))
                .build();
        DriverResponse savedDriver = this.driverService.createDriver(driverCreateDto);
        driverId = savedDriver.id();
        carId = savedDriver.carResponseDto().getFirst().id();
        System.out.println("Saved Driver: " + savedDriver);
    }

    @AfterEach
    void tearDown() {
        driverRepository.deleteAll();
        carRepository.deleteAll();
        ratingRepository.deleteAll();
    }

    @Nested
    @DisplayName("Test get by id")
    public class GetProfileByIdTests {
        @Test
        void givenDriverId_whenGetDriver_thenReturnDriverById() {
            //Act
            DriverResponse response = testRestTemplate.getForObject(DRIVER_CONTROLLER_URL + "/" + driverId, DriverResponse.class);

            //Assert
            assertThat(response).isNotNull();
            assertThat(response.id()).isEqualTo(driverId);
        }

        @Test
        void givenDriverId_whenGetDriver_thenReturnNotFound() {
            //Act
            ResponseEntity<DriverResponse> response = testRestTemplate.getForEntity(DRIVER_CONTROLLER_URL + "/" + (1 + driverId), DriverResponse.class);
            //Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Test get all by page")
    public class GetAllByPageTests {
        @Test
        void whenFindAllByPage_thenReturnContainerDriverResponse() {
            //Act
            ContainerDriverResponse containerDriverResponse = testRestTemplate.getForObject(DRIVER_CONTROLLER_URL, ContainerDriverResponse.class);

            //Assert
            assertThat(containerDriverResponse).isNotNull();
            assertThat(containerDriverResponse.totalElements()).isEqualTo(1);

        }
    }

    @Nested
    @DisplayName("Test create new driver")
    public class CreateDriverTests {
        @Test
        void givenDriverCreateDto_whenCreateDriver_thenReturnDriverResponse() {
            //Arrange
            DriverCreateDto driverCreateDto = getDriverCreateDto();
            DriverResponse expectedDriverResponse = DriverResponse.builder()
                    .id(driverId + 1L)
                    .grade(5.)
                    .username("username")
                    .email("email@gmail.com")
                    .fullName("Full Name")
                    .phoneNumber("+375331119900")
                    .gender(Gender.MALE)
                    .carResponseDto(List.of(
                            CarResponseDto.builder()
                                    .id(carId + 1L)
                                    .brand("volvo")
                                    .color("red")
                                    .registrationNumber("0021AM7")
                                    .build()
                    ))
                    .build();
            //Act
            DriverResponse actualDriverResponse = testRestTemplate.postForObject(DRIVER_CONTROLLER_URL, driverCreateDto, DriverResponse.class);
            //Assert
            assertThat(actualDriverResponse).isNotNull();
            assertThat(actualDriverResponse).isEqualTo(expectedDriverResponse);
        }

        @Test
        void givenDriverCreateDto_whenCreateDriver_thenReturnDuplicateKeyError() {
            DriverCreateDto driverCreateDto = DriverCreateDto.builder()
                    .username("test")
                    .email("test@gmail.com")
                    .fullName("Full Name")
                    .phoneNumber("+375331119912")
                    .gender("MALE")
                    .carCreateDtoList(List.of(CarCreateDto.builder()
                            .brand("volvo")
                            .color("red")
                            .registrationNumber("0001AM7")
                            .build()))
                    .build();
            ResponseEntity<DriverResponse> response = testRestTemplate.postForEntity(DRIVER_CONTROLLER_URL, driverCreateDto, DriverResponse.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    @Nested
    @DisplayName("Test update driver data")
    public class UpdateDriverDataTests {
        @Test
        void givenDriverUpdateDto_whenUpdateDriver_thenReturnDriverResponse() {
            // Arrange
            DriverUpdateDto driverUpdateDto = DriverUpdateDto.builder()
                    .username("test32")
                    .email("test32@gmail.com")
                    .fullName("Full Name1")
                    .phoneNumber("+375331119900")
                    .gender("MALE")
                    .build();
            HttpEntity<DriverUpdateDto> entity = new HttpEntity<>(driverUpdateDto);

            //Act
            ResponseEntity<DriverResponse> actual = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/" + driverId, HttpMethod.PUT, entity, DriverResponse.class);

            //Assert
            assertThat(actual).isNotNull();
            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(actual.getBody()).id()).isEqualTo(driverId);
            assertThat(actual.getBody().username()).isEqualTo("test32");
        }

        @Test
        void givenDriverUpdateDto_whenUpdateDriver_thenReturnNotFound() {
            //Arrange
            DriverUpdateDto driverUpdateDto = DriverUpdateDto.builder()
                    .username("test32")
                    .email("test32@gmail.com")
                    .fullName("Full Name1")
                    .phoneNumber("+375331119900")
                    .gender("MALE")
                    .build();
            HttpEntity<DriverUpdateDto> entity = new HttpEntity<>(driverUpdateDto);
            //Act
            ResponseEntity<DriverResponse> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/" + (driverId + 1), HttpMethod.PUT, entity, DriverResponse.class);

            //Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        void givenDriverUpdateDto_whenUpdateDriver_thenReturnDuplicateKey() {
            //Arrange
            DriverCreateDto driverCreateDto = DriverCreateDto.builder()
                    .username("test333")
                    .email("test333@gmail.com")
                    .fullName("Full Name333")
                    .phoneNumber("+375331219912")
                    .gender("MALE")
                    .carCreateDtoList(List.of(CarCreateDto.builder()
                            .brand("volvo")
                            .color("red")
                            .registrationNumber("0003AM7")
                            .build()))
                    .build();
            DriverUpdateDto driverUpdateDto = DriverUpdateDto.builder()
                    .username("test")
                    .email("test333@gmail.com")
                    .fullName("Full Name1")
                    .phoneNumber("+375331119900")
                    .gender("MALE")
                    .build();
            driverService.createDriver(driverCreateDto);
            HttpEntity<DriverUpdateDto> entity = new HttpEntity<>(driverUpdateDto);
            //Act
            ResponseEntity<DriverResponse> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/" + (driverId), HttpMethod.PUT, entity, DriverResponse.class);
            //Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

    }


    @Nested
    @DisplayName("Test delete driver")
    public class DeleteDriverTests {
        @Test
        void givenDriverId_whenDeleteDriver_thenDeleteDriverById() {
            //Act
            ResponseEntity<?> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/" + driverId,
                    HttpMethod.DELETE,
                    null,
                    DriverResponse.class);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenDriverId_whenDeleteDriver_thenReturnNotFound() {
            ResponseEntity<DriverResponse> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/" + (driverId + 1), HttpMethod.DELETE, null, DriverResponse.class);
            //Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Test create rating")
    public class CreateRatingTests {
        @Test
        void givenRatingCreateDto_whenCreateRating_thenReturnRatingResponse() {
            //Arrange
            RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                    .driverId(driverId)
                    .passengerId(1L)
                    .grade(5)
                    .build();
            HttpEntity<RatingCreateDto> entity = new HttpEntity<>(ratingCreateDto);
            //Act
            ResponseEntity<?> actual = testRestTemplate.postForEntity(DRIVER_CONTROLLER_URL + "/rating", entity, RatingCreateDto.class);
            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        }

        @Test
        void givenRatingCreateDto_whenCreateRating_thenReturnDriverNotFound() {
            //Arrange
            RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                    .driverId(driverId + 1)
                    .passengerId(1L)
                    .grade(5)
                    .build();
            HttpEntity<RatingCreateDto> entity = new HttpEntity<>(ratingCreateDto);
            //Act
            ResponseEntity<?> actual = testRestTemplate.postForEntity(DRIVER_CONTROLLER_URL + "/rating", entity, RatingCreateDto.class);
            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Test car standalone create")
    public class CreateCarTests {
        @Test
        void givenCarStandaloneCreateDto_whenAddCar_thenReturnCarResponse() {
            //Arrange
            CarStandaloneCreateDto carCreateDto = CarStandaloneCreateDto
                    .builder()
                    .driverId(driverId)
                    .brand("volvo")
                    .color("red")
                    .registrationNumber("1001AM7")
                    .build();
            //Act
            ResponseEntity<CarResponseDto> carResponseDto = testRestTemplate.postForEntity(DRIVER_CONTROLLER_URL + "/cars",
                    carCreateDto,
                    CarResponseDto.class);
            //Assert
            assertThat(carResponseDto).isNotNull();
            assertThat(carResponseDto.getBody()).isNotNull();
            assertThat(carResponseDto.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(Objects.requireNonNull(carResponseDto.getBody()).id()).isEqualTo(carId + 1);
        }
        @Test
        void givenCarStandaloneCreateDto_whenAddCar_thenReturnDuplicateKey() {
            //Arrange
            CarStandaloneCreateDto carCreateDto = CarStandaloneCreateDto
                    .builder()
                    .driverId(driverId)
                    .brand("volvo")
                    .color("red")
                    .registrationNumber("0001AM7")
                    .build();
            //Act
            ResponseEntity<CarResponseDto> carResponseDto = testRestTemplate.postForEntity(DRIVER_CONTROLLER_URL + "/cars",
                    carCreateDto,
                    CarResponseDto.class);
            //Assert
            assertThat(carResponseDto.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }


    @Nested
    @DisplayName("Test delete car")
    public class DeleteCarTests {
        @Test
        void givenCarId_whenDeleteCar_thenDeleteCarById() {
            //Act
            ResponseEntity<?> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/cars/" + carId,
                    HttpMethod.DELETE,
                    null,
                    DriverResponse.class);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }
        @Test
        void givenCarId_whenDeleteCar_thenReturnNotFound(){
            //Act
            ResponseEntity<?> response = testRestTemplate.exchange(DRIVER_CONTROLLER_URL + "/cars/" + (carId+1),
                    HttpMethod.DELETE,
                    null,
                    DriverResponse.class);

            // Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }


}
