package hillel.doctorRest.clinic.review.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import java.util.Optional;

@Data
public class ReviewInputDto {
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
