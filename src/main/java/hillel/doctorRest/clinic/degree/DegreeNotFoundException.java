package hillel.doctorRest.clinic.degree;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such a degree")
public class DegreeNotFoundException extends RuntimeException {
}
