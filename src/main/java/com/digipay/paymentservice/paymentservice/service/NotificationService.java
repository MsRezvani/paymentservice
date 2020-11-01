package com.digipay.paymentservice.paymentservice.service;

import com.digipay.paymentservice.paymentservice.notification.ConfigureRabbitMq;
import com.digipay.paymentservice.paymentservice.notification.Message;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "notifyFallback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000"),
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            }
    )
    public void notify(String dest, BigDecimal amount, Date date) {

        restTemplate.postForObject("http://notificationservice:8090/messages",
                                   new Message(dest, amount, date),
                                   Void.class);
    }

    public void notifyFallback(String dest, BigDecimal amount, Date date) {

        System.out.println("Message Send to Queue.");
        rabbitTemplate.convertAndSend(
                ConfigureRabbitMq.EXCHANGE_NAME,
                "NOTIFICATION.#",
                new Message(dest, amount, date));
    }
}
