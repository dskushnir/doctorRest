package hillel.doctorRest;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;


@SpringBootApplication
@EnableRetry

public class DoctorRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorRestApplication.class, args);

	}
}
