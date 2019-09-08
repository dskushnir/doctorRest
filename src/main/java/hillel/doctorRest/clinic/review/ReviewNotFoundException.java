package hillel.doctorRest.clinic.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such a review")
public class ReviewNotFoundException extends RuntimeException {
}
