package hillel.doctorRest.clinic.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This hour is not available for making an appointment with a doctor.Please, choose another hour ")
public class HourNotFoundAvailableException extends RuntimeException {
}
