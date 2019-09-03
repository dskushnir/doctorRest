package hillel.doctorRest.clinic.review;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect effectiveness of treatment rating")
public class EffectivenessOfTreatmentRatingExeption extends RuntimeException {
}
