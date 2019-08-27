package hillel.doctorRest.clinic.schedule.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ScheduleOutputDto {
    private Integer id;
    private LocalDate visitDate;
    private Integer doctorId;
    private String hour;
    private String petId;

}
