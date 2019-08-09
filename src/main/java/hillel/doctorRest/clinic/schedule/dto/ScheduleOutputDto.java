package hillel.doctorRest.clinic.schedule.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleOutputDto {
    private Integer id;
    private LocalDateTime visitDate;
    private Integer doctorId;
    private String hour;
    private String petId;
}
