package com.example.rides.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic addRatingTopic() {
        return new NewTopic("rides-notification-topic", 3, (short) 1);
    }

}
