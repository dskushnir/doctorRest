package hillel.doctorRest.clinic.review.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import java.util.Optional;

@Data
public class ReviewInputDto {
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

    public Optional<Integer> getService() {
        return Optional.ofNullable(service);
    }

    public Optional<Integer> getEquipment() {
        return Optional.ofNullable(equipment);
    }

    public Optional<Integer> getQualificationSpecialist() {
        return Optional.ofNullable(qualificationSpecialist);
    }

    public Optional<Integer> getEffectivenessOfTreatment() {
        return Optional.ofNullable(effectivenessOfTreatment);
    }

    public Optional<Integer> getRatingOverall() {
        return Optional.ofNullable(ratingOverall);
    }

    public Optional<String> getComment() {
        return Optional.ofNullable(comment);
    }
}
