package hillel.doctorRest.clinic.degree.dto;

import hillel.doctorRest.clinic.degree.Degree;
import org.mapstruct.Mapper;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Mapper

public interface DegreeModelConverter {
    DegreeOutputDto toDegreeDto (Degree degree);



}
