package hillel.doctorRest.clinic.schedule.dto;

import hillel.doctorRest.clinic.schedule.Schedule;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ScheduleModelConverter {
    ScheduleOutputDto toDto(Schedule schedule);

    List<ScheduleOutputDto> schedulesToOutputDto(List<Schedule> schedules);
}
