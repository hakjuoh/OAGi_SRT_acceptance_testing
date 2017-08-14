package org.oagi.srt.uat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class AcceptanceTestApplication {

    public static void main(String[] args) throws Exception {
        try (ConfigurableApplicationContext applicationContext =
                     SpringApplication.run(AcceptanceTestApplication.class, args)) {

        }
    }

}
