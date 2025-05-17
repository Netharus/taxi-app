package com.example.driver.kafka;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.dto.RideResponseForDriver;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final DriverService driverService;

    @RetryableTopic
    @KafkaListener(topics = "driver-rating-topic")
    @Transactional
    public void consumeRating(RatingCreateDto ratingCreateDto) {
        driverService.addRating(ratingCreateDto);
    }

    @RetryableTopic
    @KafkaListener(topics = "driver-ride-notification-topic")
    @Transactional
    public void consumeRideNotification(RideResponseForDriver rideResponseForDriver) {
        driverService.notifyDriver(rideResponseForDriver);
    }

    @KafkaListener(topics = "driver-notification-topic")
    public void consumeNotification(RideResponseForDriver rideResponseForDriver) {
        driverService.notifyAboutEndDriver(rideResponseForDriver);
    }

    @DltHandler
    public void consumeDLT() {
        logger.error("Something went wrong");
    }
}
