package hillel.doctorRest.clinic.degree;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Degree present")
public class DegreeMatchException extends RuntimeException {
}
