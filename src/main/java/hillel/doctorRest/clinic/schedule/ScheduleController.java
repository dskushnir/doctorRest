package hillel.doctorRest.clinic.schedule;
import hillel.doctorRest.clinic.WisitHoursConfig;
import hillel.doctorRest.clinic.doctor.DoctorNotFoundException;
import hillel.doctorRest.clinic.doctor.DoctorService;
import hillel.doctorRest.clinic.pet.PetNotFoundException;
import hillel.doctorRest.clinic.pet.PetService;
import hillel.doctorRest.clinic.schedule.dto.ScheduleDtoConverter;
import hillel.doctorRest.clinic.schedule.dto.ScheduleInputDto;
import hillel.doctorRest.clinic.schedule.dto.ScheduleModelConverter;
import hillel.doctorRest.clinic.schedule.dto.ScheduleOutputDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor

public class ScheduleController {
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final PetService petService;
    private WisitHoursConfig wisitHoursConfig;
    private final ScheduleDtoConverter scheduleDtoConverter;
    private final ScheduleModelConverter scheduleModelConverter;

    @GetMapping("/doctors/doctorId/schedule/visitDate")
    @ResponseStatus(HttpStatus.OK)
    public List<ScheduleOutputDto> findByDoctorIdAndVisitDate(@RequestParam Integer doctorId,
                                                              @RequestParam LocalDateTime visitDate) {
        if (doctorService.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException();
        } else {
            val scheduleByDoctor = scheduleService.findByDoctorIdAndVisitDate(doctorId, visitDate);
            return scheduleByDoctor.stream()
                    .map(schedule -> scheduleModelConverter.toDto(schedule))
                    .collect(Collectors.toList());
        }
    }

    @PostMapping("/doctors/{doctorId}/schedule/{createDate}/{hour}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSchedule(@PathVariable Integer doctorId,
                               @RequestBody ScheduleInputDto scheduleInputDto,
                               @PathVariable LocalDateTime visitDate,
                               @PathVariable String hour) {
        if (doctorService.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException();
        } else if (petService.findById(Integer.valueOf(scheduleInputDto.getPetId())).isEmpty()) {
            throw new PetNotFoundException();
        } else if (!wisitHoursConfig.getHourName().contains(hour)) {
            throw new HourNotFoundException();
        } else if (scheduleService.findByHour(hour).isPresent()) {
            throw new HourNotFoundAvailableException();

        } else {
            val scheduleModel = scheduleDtoConverter.toModel(scheduleInputDto);
            scheduleService.createSchedule(doctorId, visitDate, hour, scheduleModel);
        }
    }
}
