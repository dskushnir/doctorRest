package hillel.doctorRest.clinic.review.dto;

import lombok.Data;

@Data

public class ReviewInputForUpdateDto {
    private Integer service;
    private Integer equipment;
    private Integer qualificationSpecialist;
    private Integer effectivenessOfTreatment;
    private Integer ratingOverall;
    private String comment;
}
