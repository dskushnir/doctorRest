package hillel.doctorRest.clinic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("clinic.hours")

public class WisitHoursConfig {
    List<String>hourName=new ArrayList<>();
}
