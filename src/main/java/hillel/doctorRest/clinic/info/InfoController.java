package hillel.doctorRest.clinic.info;

import hillel.doctorRest.clinic.SpecializationConfig;
import hillel.doctorRest.clinic.VisitHoursConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class InfoController {
    private final String clinicName;
    private final SpecializationConfig specializationConfig;
    private final VisitHoursConfig visitHoursConfig;
    private final Clock clock;

    public InfoController(@Value("${clinic.name}") String clinicName,
                          SpecializationConfig specializationConfig,
                          VisitHoursConfig visitHoursConfig, Clock clock) {
        this.clinicName = clinicName;
        this.specializationConfig = specializationConfig;
        this.visitHoursConfig = visitHoursConfig;
         this.clock = clock;
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

    @GetMapping("/time")
    public LocalDateTime time() {
        return LocalDateTime.now(clock);
    }
}
