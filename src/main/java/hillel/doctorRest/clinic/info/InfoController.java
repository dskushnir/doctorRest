package hillel.doctorRest.clinic.info;

import hillel.doctorRest.clinic.SpecializationConfig;
import hillel.doctorRest.clinic.VisitHoursConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class InfoController {
    private final String clinicName;
    private final SpecializationConfig specializationConfig;
    private final VisitHoursConfig visitHoursConfig;

    public InfoController(@Value("${clinic.name}") String clinicName,
                          SpecializationConfig specializationConfig,
                          VisitHoursConfig visitHoursConfig) {
        this.clinicName = clinicName;
        this.specializationConfig = specializationConfig;
        this.visitHoursConfig = visitHoursConfig;
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
        return visitHoursConfig.getHourName();
    }
}
