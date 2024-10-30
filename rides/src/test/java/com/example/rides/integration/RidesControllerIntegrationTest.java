package com.example.rides.integration;

import com.example.rides.dto.RidesInformationResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RidesControllerIntegrationTest {

    private static final String RIDES_CONTROLLER_URL = "/api/v1/rides";

    @Autowired
    TestRestTemplate testRestTemplate;

    @Container
    @ServiceConnection
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:14.3");

    @Container
    @ServiceConnection
    static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:latest")
    );

    @Nested
    @DisplayName("Test check rides info")
    public class CheckRidesInfoTests {
        @Test
        void givenStartPointEndPoint_whenCheckRidesInformation_thenReturnCheckRidesInformation(){
            //Arrange
            String startPoint = "минск проспект газеты правда 22";
            String endPoint = "минск проспект дзержинского 198";
            //Act
            ResponseEntity<RidesInformationResponseDto> response = testRestTemplate.getForEntity(
                    RIDES_CONTROLLER_URL + "?startPoint=" + startPoint + "&endPoint=" + endPoint,
                    RidesInformationResponseDto.class);
            //Assert
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response).isNotNull();
        }
    }
}
