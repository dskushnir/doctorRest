package hillel.doctorRest.clinic.degree.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
@Data
public class DegreeInputDto {
    @NotNull
    @Positive
    private Integer degreeNumber;
    @NotBlank
    private String indicationEducationalEstablishment;
    @NotBlank
    private String specialty;
    @PastOrPresent
    private LocalDate dateCompletionOfStudy;
}
