package hillel.doctorRest.doctor;

        import org.springframework.http.HttpStatus;
        import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Mismatched Id")
public class IdMisMatchException extends RuntimeException {
}
