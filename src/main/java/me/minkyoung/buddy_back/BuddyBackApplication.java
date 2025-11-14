package me.minkyoung.buddy_back;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing //생성일/수정일 자동 기록 기능
public class BuddyBackApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuddyBackApplication.class, args);
    }

}
