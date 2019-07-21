package hillel.doctorRest.doctor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)

public class IdMissMatchException extends RuntimeException{
}
