package org.onlinetestsystem.onlinetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.onlinetestsystem.onlinetest.repository") // Specify the package containing your JPA repositories

public class OnlinetestApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinetestApplication.class, args);
    }
}
