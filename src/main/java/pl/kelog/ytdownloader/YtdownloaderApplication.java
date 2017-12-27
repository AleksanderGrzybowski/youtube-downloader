package pl.kelog.ytdownloader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class YtdownloaderApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(YtdownloaderApplication.class, args);
    }
}

