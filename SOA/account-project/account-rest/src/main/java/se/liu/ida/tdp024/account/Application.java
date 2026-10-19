package se.liu.ida.tdp024.account;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"se.liu.ida.tdp024"})
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}