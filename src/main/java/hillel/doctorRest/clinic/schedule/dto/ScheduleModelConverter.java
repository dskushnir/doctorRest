package hillel.doctorRest.clinic.schedule.dto;

import hillel.doctorRest.clinic.pet.Pet;
import hillel.doctorRest.clinic.pet.dto.PetOutputDto;
import hillel.doctorRest.clinic.schedule.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


@Mapper
public interface ScheduleModelConverter {
    ScheduleOutputDto  toDto (Schedule schedule);
}
