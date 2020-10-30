package com.digipay.paymentservice.paymentservice;

import brave.sampler.Sampler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableCircuitBreaker
@EnableHystrix
@EnableSwagger2
public class PaymentServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(PaymentServiceApplication.class, args);
    }

    @Bean
    public RestTemplate getRestTemplate() {

        return new RestTemplate();
    }

    @Bean
    public Sampler defaultSampler() {

        return Sampler.ALWAYS_SAMPLE;
    }
}
