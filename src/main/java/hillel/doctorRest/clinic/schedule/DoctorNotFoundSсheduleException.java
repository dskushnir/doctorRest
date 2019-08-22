package hillel.doctorRest.clinic.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The sick doctor has not admission on this date")
public class DoctorNotFoundSсheduleException extends RuntimeException {
}
