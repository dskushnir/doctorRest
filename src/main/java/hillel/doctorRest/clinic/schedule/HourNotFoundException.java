package hillel.doctorRest.clinic.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Not such the hour in  the schedule")
public class HourNotFoundException extends RuntimeException {
}
