package ru.notivent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = {"ru.notivent"})
@ComponentScan("ru.notivent")
public class NotiventApp {
    public static void main(String[] args) {
        SpringApplication.run(NotiventApp.class, args);
    }
}