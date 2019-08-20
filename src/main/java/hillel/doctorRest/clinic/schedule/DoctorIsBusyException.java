package hillel.doctorRest.clinic.schedule;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Doctor is busy")

public class DoctorIsBusyException extends Exception {
}
