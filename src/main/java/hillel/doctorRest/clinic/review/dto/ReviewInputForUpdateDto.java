package hillel.doctorRest.clinic.review.dto;

import lombok.Data;

import javax.validation.constraints.Max;

@Data

public class ReviewInputForUpdateDto {
    @Max(5)
    private Integer service;
    @Max(5)
    private Integer equipment;
    @Max(5)
    private Integer qualificationSpecialist;
    @Max(5)
    private Integer effectivenessOfTreatment;
    @Max(5)
    private Integer ratingOverall;
    private String comment;
}
