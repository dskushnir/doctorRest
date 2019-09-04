package hillel.doctorRest.clinic.doctor;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,  ElementType.TYPE_USE})
@Constraint(validatedBy = SpecializationNameValidator.class)
public @interface SpecializationName {
    String message() default "Invalid specialization name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
