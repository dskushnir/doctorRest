package hillel.doctorRest.clinic.info;

import hillel.doctorRest.clinic.SpecializationConfig;
import hillel.doctorRest.clinic.WisitHoursConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InfoController {
    private final String clinicName;
    private final SpecializationConfig specializationConfig;
    private final WisitHoursConfig wisitHoursConfig;

    public InfoController(@Value("${clinic.name}") String clinicName,
                          SpecializationConfig specializationConfig,
                          WisitHoursConfig wisitHoursConfig) {
        this.clinicName = clinicName;
        this.specializationConfig = specializationConfig;
        this.wisitHoursConfig = wisitHoursConfig;
    }

    @GetMapping("/clinic-name")
    public String getName() {
        return clinicName;
    }

    @GetMapping("/doctors-specializations")
    public List<String> getSpecializations() {
        return specializationConfig.getSpecializationName();
    }
    @GetMapping("/doctors-hours")
    public List<String> getHours(){
        return wisitHoursConfig.getHourName();
    }
}
