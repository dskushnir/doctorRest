package hillel.doctorRest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.annotation.EnableRetry;

import java.time.Clock;


@SpringBootApplication
@EnableRetry

public class DoctorRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorRestApplication.class, args);

	}
	@Bean
	public Clock clock() {
		return Clock.systemUTC();
	}
}
