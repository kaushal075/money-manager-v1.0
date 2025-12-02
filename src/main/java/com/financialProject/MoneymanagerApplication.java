package com.financialProject;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneymanagerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(MoneymanagerApplication.class, args);

        // Print active profiles for verification
        String[] activeProfiles = context.getEnvironment().getActiveProfiles();
        if (activeProfiles.length == 0) {
            System.out.println("No active profile set, using default properties.");
        } else {
            System.out.println("Active Spring profiles: " + Arrays.toString(activeProfiles));
        }
//        System.out.println(System.getenv("BREVO_API_KEY"));
//        System.out.println(System.getenv("BREVO_SENDER_EMAIL"));

    }
}
