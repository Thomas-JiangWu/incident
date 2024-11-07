package hsbc.incident;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"hsbc.incident.mapper"})
public class IncidentApplication {

    public static void main(String[] args) {
        SpringApplication.run(IncidentApplication.class, args);
    }

}
