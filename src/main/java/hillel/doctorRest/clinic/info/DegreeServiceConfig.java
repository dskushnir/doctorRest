package hillel.doctorRest.clinic.info;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Data
@ConfigurationProperties("clinic")
@Component
@Validated
public class DegreeServiceConfig {
    @NotBlank
    private String degreeUrl;
}
