package com.knowledge.graph;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@Slf4j
@MapperScan("com.knowledge.graph.store.mapper")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        try {
            Runtime.getRuntime().exec("cmd /c start http://localhost:8080");
            Runtime.getRuntime().exec("cmd /c start http://localhost:8080/doc.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
