package top.tqx.week05;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("top.tqx.week05.mapper")
public class Week05Application {

    public static void main(String[] args) {
        SpringApplication.run(Week05Application.class, args);
    }

}
