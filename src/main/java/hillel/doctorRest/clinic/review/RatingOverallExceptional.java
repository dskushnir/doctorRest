package hillel.doctorRest.clinic.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect qualification overall rating")
public class RatingOverallExceptional extends RuntimeException {
}
