package com.example.passenger.kafka;


import com.example.passenger.dto.RatingCreateDto;
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


    private final static String DRIVER_RATING_TOPIC = "driver-rating-topic";

    private final static String DRIVER_NOTIFICATION_TOPIC = "driver-notification-topic";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    public void sendMessage(RatingCreateDto ratingCreateDto) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate
                .send(DRIVER_RATING_TOPIC, generateTransactionalKey(), ratingCreateDto);
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Sent message successfully{}with offset{}", ratingCreateDto.toString(), result.getRecordMetadata().offset());
            } else {
                logger.error(ex.getMessage());
            }
        });
    }

    private String generateTransactionalKey() {
        return UUID.randomUUID().toString();
    }
}
