package space.astralbridge.spring.moviehub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
public class MovieHubApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieHubApplication.class, args);
    }
}
