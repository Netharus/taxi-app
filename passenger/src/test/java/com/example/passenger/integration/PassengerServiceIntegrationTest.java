package com.example.passenger.integration;


import com.example.passenger.dto.ContainerPassengerResponseDto;
import com.example.passenger.dto.PassengerCreateDto;
import com.example.passenger.dto.PassengerResponseDto;
import com.example.passenger.dto.PassengerUpdateDto;
import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.repository.PassengerRepository;
import com.example.passenger.repository.RatingRepository;
import com.example.passenger.service.PassengerService;
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

import java.util.Objects;

import static com.example.passenger.integration.util.PassengerIntegrationTestUtil.getPassengerCreateDto;
import static com.example.passenger.integration.util.PassengerIntegrationTestUtil.getPassengerUpdateDto;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PassengerServiceIntegrationTest {

    private static final String PASSENGER_CONTROLLER_URL = "/api/v1/passengers";

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3");

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @Autowired
    PassengerService passengerService;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    RatingRepository ratingRepository;

    private Long passengerId;

    @BeforeEach
    void setUp() {
        PassengerCreateDto passengerCreateDto = PassengerCreateDto
                .builder()
                .email("email21@email.com")
                .firstName("firstName21")
                .phoneNumber("+375293631111")
                .build();
        PassengerResponseDto passengerResponseDto = passengerService.createPassenger(passengerCreateDto);
        passengerId = passengerResponseDto.id();

    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
        passengerRepository.deleteAll();
    }

    @Nested
    @DisplayName("Test create passenger")
    public class CreatePassenger {
        @Test
        void givenPassengerCreateDto_whenCreatePassenger_thenReturnPassengerResponseDto() {
            //Arrange
            PassengerCreateDto passengerCreateDto = getPassengerCreateDto();
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate
                    .postForEntity(PASSENGER_CONTROLLER_URL,
                            passengerCreateDto,
                            PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(Objects.requireNonNull(actual.getBody()).id()).isEqualTo(passengerId + 1);
        }

        @Test
        void givenPassengerCreateDto_whenCreatePassenger_thenReturnDuplicateKey(){
            //Arrange
            PassengerCreateDto passengerCreateDto = PassengerCreateDto.builder()
                    .email("email21@email.com")
                    .firstName("firstName21")
                    .phoneNumber("+375293631111")
                    .build();
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate
                    .postForEntity(PASSENGER_CONTROLLER_URL,
                            passengerCreateDto,
                            PassengerResponseDto.class);
            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }

    @Nested
    @DisplayName("Test update passenger")
    public class UpdatePassenger {
        @Test
        void givenPassengerUpdateDto_whenUpdatePassenger_thenReturnPassengerResponseDto() {
            //Arrange
            PassengerUpdateDto passengerUpdateDto = getPassengerUpdateDto();

            HttpEntity<PassengerUpdateDto> entity = new HttpEntity<>(passengerUpdateDto);
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate
                    .exchange(PASSENGER_CONTROLLER_URL+"/"+passengerId, HttpMethod.PUT, entity, PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(actual.getBody()).email()).isEqualTo(passengerUpdateDto.email());
        }

        @Test
        void givenPassengerUpdateDto_whenUpdatePassenger_thenReturnDuplicateKey(){
            //Arrange
            PassengerUpdateDto passengerUpdateDto = getPassengerUpdateDto();
            PassengerCreateDto passengerCreateDto = PassengerCreateDto.builder()
                    .email(passengerUpdateDto.email())
                    .firstName(passengerUpdateDto.firstName())
                    .phoneNumber(passengerUpdateDto.phoneNumber())
                    .build();
            passengerService.createPassenger(passengerCreateDto);

            HttpEntity<PassengerUpdateDto> entity = new HttpEntity<>(passengerUpdateDto);

            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate
                    .exchange(PASSENGER_CONTROLLER_URL+"/"+passengerId, HttpMethod.PUT, entity, PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }

        @Test
        void givenPassengerUpdateDto_whenUpdatePassenger_thenReturnNotFound(){
            //Arrange
            PassengerUpdateDto passengerUpdateDto = getPassengerUpdateDto();
            HttpEntity<PassengerUpdateDto> entity = new HttpEntity<>(passengerUpdateDto);
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate
                    .exchange(PASSENGER_CONTROLLER_URL+"/"+(passengerId+1), HttpMethod.PUT, entity, PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }
    }

   @Nested
   @DisplayName("Test get passenger by id")
   public class GetPassengerById {
       @Test
       void givenPassengerId_whenGetPassenger_thenReturnPassengerResponseDto() {
           //Act
           ResponseEntity<PassengerResponseDto> actual =testRestTemplate.getForEntity(PASSENGER_CONTROLLER_URL+"/"+passengerId, PassengerResponseDto.class);

           //Assert
           assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
           assertThat(Objects.requireNonNull(actual.getBody()).id()).isEqualTo(passengerId);
       }

       @Test
       void givenPassengerId_whenGetPassenger_thenReturnNotFound() {
           //Act
           ResponseEntity<PassengerResponseDto> actual =testRestTemplate.getForEntity(PASSENGER_CONTROLLER_URL+"/"+(passengerId+1), PassengerResponseDto.class);

           //Assert
           assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
       }
   }

    @Nested
    @DisplayName("Test delete passenger")
    public class DeletePassenger {
        @Test
        void givenPassengerId_whenDeletePassenger_thenDeletePassenger() {
            //Act
            ResponseEntity<?> actual=testRestTemplate.exchange(PASSENGER_CONTROLLER_URL+"/"+passengerId, HttpMethod.DELETE, null, PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        }

        @Test
        void givenPassengerId_whenDeletePassenger_thenReturnNotFound() {
            //Act
            ResponseEntity<?> actual=testRestTemplate.exchange(PASSENGER_CONTROLLER_URL+"/"+(passengerId+1), HttpMethod.DELETE, null, PassengerResponseDto.class);

            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }

    @Nested
    @DisplayName("Test find all passengers by page")
    public class FindAll {
        @Test
        void whenFindAll_thenReturnContainerPassengerResponseDto() {
            //Act
            ContainerPassengerResponseDto actual = testRestTemplate.getForObject(PASSENGER_CONTROLLER_URL, ContainerPassengerResponseDto.class);
            //Assert
            assertThat(actual.totalElements()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Test created rating")
    public class CreateRating {
        @Test
        void givenRatingCreateDto_whenCreateRating_thenReturnRatingResponse() {
            //Arrange
            RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                    .driverId(1L)
                    .passengerId(passengerId)
                    .grade(1)
                    .build();
            HttpEntity<RatingCreateDto> entity = new HttpEntity<>(ratingCreateDto);
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate.postForEntity(PASSENGER_CONTROLLER_URL + "/rating", entity, PassengerResponseDto.class);
            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(actual.getBody()).isNotNull();
            assertThat(Objects.requireNonNull(actual.getBody()).grade()).isEqualTo(3);
        }


        @Test
        void givenRatingCreateDto_whenCreateRating_thenReturnPassengerNotFound() {
            //Arrange
            RatingCreateDto ratingCreateDto = RatingCreateDto.builder()
                    .driverId(1L)
                    .passengerId(passengerId+1)
                    .grade(1)
                    .build();
            HttpEntity<RatingCreateDto> entity = new HttpEntity<>(ratingCreateDto);
            //Act
            ResponseEntity<PassengerResponseDto> actual = testRestTemplate.postForEntity(PASSENGER_CONTROLLER_URL + "/rating", entity, PassengerResponseDto.class);
            //Assert
            assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

    }
}
