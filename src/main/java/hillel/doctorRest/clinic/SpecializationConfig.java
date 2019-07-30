package hillel.doctorRest.clinic;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@ConfigurationProperties("clinic.specializations")
public class SpecializationConfig {
    private List<String> specializationName = new ArrayList<>();
}
