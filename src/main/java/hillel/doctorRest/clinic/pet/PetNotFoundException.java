package hillel.doctorRest.clinic.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "No such a pet")

public class PetNotFoundException extends RuntimeException{
}
