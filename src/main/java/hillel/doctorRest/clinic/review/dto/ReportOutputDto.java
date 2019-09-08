package hillel.doctorRest.clinic.review.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ReportOutputDto {
    public double averageService;
    public double averageEquipment;
    public double averageQualificationSpecialist;
    public double averageEffectivenessOfTreatment;
    public double averageRatingOverall;
    public Map<LocalDateTime, List<String>> mapDateToComment;
}
