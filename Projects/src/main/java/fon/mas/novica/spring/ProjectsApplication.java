package fon.mas.novica.spring;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@EnableDiscoveryClient
@Slf4j
@EnableFeignClients
public class ProjectsApplication {

    @Value("${config.source}")
    String configSource;

    public static void main(String[] args) {
        SpringApplication.run(ProjectsApplication.class, args);
    }
    @EventListener(ApplicationReadyEvent.class)
    void configServerEchoTest(){
        log.info("======== REACHING CONFIG SERVER PROPERTIES ========");
        if ("Local".equalsIgnoreCase(configSource)) {
            log.warn("Config source is Local!");
        } else {
            log.info("Config source: " + configSource);
        }
        log.info("===================================================");
    }

    @Bean
    ModelMapper mapper(){
        return new ModelMapper();
    }

    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }

}
