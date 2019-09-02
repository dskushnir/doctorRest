package hillel.doctorRest.clinic.review.dto;

import lombok.Data;
import java.util.Optional;

@Data
public class ReviewInputDto {
    private Integer service;
    private Integer equipment;
    private Integer qualificationSpecialist;
    private Integer effectivenessOfTreatment;
    private Integer ratingOverall;
    private String comment;

    public Optional<Integer> getService() {
        return Optional.ofNullable(service);
    }
    public Optional<Integer>getEquipment(){
        return Optional.ofNullable(equipment);
    }
    public Optional<Integer>getQualificationSpecialist(){
        return Optional.ofNullable(qualificationSpecialist);
    }
    public Optional<Integer>getEffectivenessOfTreatment(){
        return Optional.ofNullable(effectivenessOfTreatment);
    }
    public Optional<Integer>getRatingOverall(){
        return Optional.ofNullable(ratingOverall);
    }
    public Optional<String>getComment(){
        return Optional.ofNullable(comment);
    }

}
