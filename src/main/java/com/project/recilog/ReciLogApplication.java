package com.project.recilog; // 메인 패키지 경로 선언

import org.springframework.boot.SpringApplication; // Spring Boot 실행 클래스 임포트
import org.springframework.boot.autoconfigure.SpringBootApplication; // Spring Boot 자동 설정 어노테이션 임포트

@SpringBootApplication // @Configuration, @EnableAutoConfiguration, @ComponentScan 3가지를 합친 통합 어노테이션
public class ReciLogApplication { // 애플리케이션 시작점 클래스 정의
    public static void main(String[] args) { // 자바 프로그램의 메인 진입 메서드
        SpringApplication.run(ReciLogApplication.class, args); // 내장 톰캣 서버를 구동하고 Spring 컨테이너를 초기화하여 실행
    }
}