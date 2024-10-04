package com.example.driver.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfiguration {

    @Bean
    public NewTopic addRatingTopic() {
        return new NewTopic("driver-rating-topic", 3, (short) 1);
    }

    @Bean
    public NewTopic addDriverNotificationTopic() {
        return new NewTopic("driver-notification-topic", 3, (short) 1);
    }
    @Bean
    public NewTopic addDriverRideNotificationTopic() {
        return new NewTopic("driver-ride-notification-topic", 3, (short) 1);
    }
}
