package fon.mas.novica.spring.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
@Slf4j
public class NotificationsApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationsApplication.class, args);
    }


    @Value("${config.source}")
    String configSource;
    @EventListener(ApplicationReadyEvent.class)
    void configServerEchoTest(){
        log.info("====== REACHING CONFIG SERVER PROPERTIES =====");
        if ("Local".equalsIgnoreCase(configSource)) {
            log.warn("Config source is Local!");
        } else {
            log.info("Config source: " + configSource);
        }
        log.info("==============================================");
    }
}
