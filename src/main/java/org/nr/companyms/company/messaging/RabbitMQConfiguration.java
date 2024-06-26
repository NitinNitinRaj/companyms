package org.nr.companyms.company.messaging;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonDecoder;

@Configuration
public class RabbitMQConfiguration {
    @Bean //Creating a queue
    public Queue companyRatingQueue() {
        return new Queue("companyRatingQueue");
    }

    @Bean //Creating a converter for object to json.
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean //Handles creation and release of resource from rabbitmq while sending and receiving message
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory); //for creating connection
        rabbitTemplate.setMessageConverter(jsonMessageConverter()); // added jackson support
        return rabbitTemplate;
    }
}