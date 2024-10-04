package com.example.driver.kafka;

import com.example.driver.dto.RatingCreateDto;
import com.example.driver.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final DriverService driverService;

    @KafkaListener(topics = "driver-rating-topic")
    public void consumeRating(RatingCreateDto ratingCreateDto) {
        driverService.addRating(ratingCreateDto);
    }

    @KafkaListener(topics = "driver-notification-topic")
    public void consumeNotification() {
    }
}
