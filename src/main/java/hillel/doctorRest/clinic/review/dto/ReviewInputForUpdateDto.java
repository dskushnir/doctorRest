package hillel.doctorRest.clinic.review.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;
@Data

public class ReviewInputForUpdateDto {
    @Range(min = 1,max = 5)
    private Integer service;
    @Range(min = 1,max = 5)
    private Integer equipment;
    @Range(min = 1,max = 5)
    private Integer qualificationSpecialist;
    @Range(min = 1,max = 5)
    private Integer effectivenessOfTreatment;
    @Range(min = 1,max = 5)
    private Integer ratingOverall;
    private String comment;
}
