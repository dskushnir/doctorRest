package hillel.doctorRest.clinic.degree.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DegreeOutputDto {
    private Integer id;
    private Integer degreeNumber;
    private String indicationEducationalEstablishment;
    private String specialty;
    private LocalDate dateCompletionOfStudy;
}
