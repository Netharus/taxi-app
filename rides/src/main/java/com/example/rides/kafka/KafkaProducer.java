package com.example.rides.kafka;

import com.example.rides.dto.RideCreateResponseDto;
import com.example.rides.dto.RideResponseForDriver;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class KafkaProducer {

    private final static String PASSENGER_NOTIFICATION_TOPIC = "passenger-notification-topic";

    private final static String DRIVER_NOTIFICATION_TOPIC = "driver-notification-topic";

    private final static String DRIVER_RIDE_NOTIFICATION_TOPIC = "driver-ride-notification-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public void sendRideResponseToDriver(RideResponseForDriver rideResponseForDriver) {
        sendMessage(DRIVER_RIDE_NOTIFICATION_TOPIC, generateTransactionalKey(), rideResponseForDriver);
    }

    public void notifyPassenger(RideCreateResponseDto rideCreateResponseDto) {
        sendMessage(PASSENGER_NOTIFICATION_TOPIC, generateTransactionalKey(), rideCreateResponseDto);
    }

    public void notifyDriver(RideResponseForDriver rideResponseForDriver) {
        sendMessage(DRIVER_NOTIFICATION_TOPIC, generateTransactionalKey(), rideResponseForDriver);
    }

    private void sendMessage(String topic, String key, Object message) {
        CompletableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(topic, key, message);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message successfully{}with offset{}",
                        message.toString(),
                        result.getRecordMetadata().offset());
            } else {
                logger.error(ex.getMessage());
            }
        });
    }

    private String generateTransactionalKey() {
        return UUID.randomUUID().toString();
    }
}
