package hillel.doctorRest.clinic.schedule.dto;

import hillel.doctorRest.clinic.schedule.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper

public interface ScheduleDtoConverter {
    @Mappings({@Mapping(target = "id", ignore = true),
            @Mapping(target = "doctorId", ignore = true),
            @Mapping(target = "visitDate", ignore = true),
            @Mapping(target = "hour", ignore = true)})
    Schedule toModel(ScheduleInputDto scheduleInputDto);
}
