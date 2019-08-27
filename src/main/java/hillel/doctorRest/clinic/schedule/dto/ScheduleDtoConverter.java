package hillel.doctorRest.clinic.schedule.dto;

import hillel.doctorRest.clinic.schedule.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.LocalDate;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ScheduleDtoConverter {
    @Mapping(target = "id", ignore = true)
    Schedule toModel(Integer doctorId, LocalDate visitDate, String hour, ScheduleInputDto scheduleInputDto);

}
