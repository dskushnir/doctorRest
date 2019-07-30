package hillel.doctorRest.clinic.info;

import hillel.doctorRest.clinic.SpecializationConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InfoController {
    private final String clinicName;
    private final SpecializationConfig specializationConfig;

    public InfoController(@Value("${clinic.name}") String clinicName,
                          SpecializationConfig specializationConfig) {
        this.clinicName = clinicName;
        this.specializationConfig = specializationConfig;
    }

    @GetMapping("/clinic-name")
    public String getName() {
        return clinicName;
    }

    @GetMapping("/doctors-specializations")
    public List<String> getSpecializations() {
        return specializationConfig.getSpecializationName();
    }
}
