package com.example.passenger.kafka;


import com.example.passenger.dto.RatingCreateDto;
import com.example.passenger.service.PassengerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    private final PassengerService passengerService;

    @KafkaListener(topics = "passenger-rating-topic")
    public void consumeRating(RatingCreateDto ratingCreateDto) {
        passengerService.addRating(ratingCreateDto);
    }

    @KafkaListener(topics = "passenger-notification-topic")
    public void consumeNotification() {
    }
}
