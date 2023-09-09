package com.example.ttsxscheduling;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.ttsx.feignApi"})
public class TtsxSchedulingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtsxSchedulingApplication.class, args);
    }

}
