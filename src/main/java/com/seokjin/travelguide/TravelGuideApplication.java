package com.seokjin.travelguide;

import com.seokjin.travelguide.repository.MemberRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TravelGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(TravelGuideApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return new TestDataInit(memberRepository, bCryptPasswordEncoder);
    }
}
