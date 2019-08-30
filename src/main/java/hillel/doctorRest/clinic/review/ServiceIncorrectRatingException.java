package hillel.doctorRest.clinic.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect service rating")
public class ServiceIncorrectRatingException extends RuntimeException {
}
