package com.example.passenger.kafka;


import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.dto.RideCreateResponseDto;
import com.example.passenger.model.enums.Status;
import com.example.passenger.service.PassengerService;
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

    private final PassengerService passengerService;

    @RetryableTopic
    @KafkaListener(topics = "passenger-rating-topic")
    @Transactional
    public void consumeRating(RatingCreateDto ratingCreateDto) {
        passengerService.addRating(ratingCreateDto);
    }

    @RetryableTopic
    @KafkaListener(topics = "passenger-notification-topic")
    public void consumeNotification(RideCreateResponseDto rideCreateResponseDto) {
        if (rideCreateResponseDto.status().equals(Status.COMPLETED)) {
            passengerService.notifyAboutEnding(rideCreateResponseDto);
        } else {
            passengerService.notifyPassenger(rideCreateResponseDto);
        }
    }

    @DltHandler
    public void consumeDLT() {
        logger.error("Something went wrong");
    }
}
