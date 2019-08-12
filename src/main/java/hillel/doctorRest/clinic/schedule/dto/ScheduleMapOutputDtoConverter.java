package hillel.doctorRest.clinic.schedule.dto;

import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Mapper

public class ScheduleMapOutputDtoConverter {

    public Map<String, String> hourToPetId(List<ScheduleOutputDto> schedulesToOutputDto) {
        if (schedulesToOutputDto == null) {
            return null;
        } else {
            return schedulesToOutputDto.stream().
                    collect(Collectors.toMap(
                            ScheduleOutputDto::getHour,
                            ScheduleOutputDto::getPetId));
        }
    }
}
