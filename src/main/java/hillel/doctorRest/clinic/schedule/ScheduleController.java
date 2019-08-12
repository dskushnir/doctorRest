package hillel.doctorRest.clinic.schedule;
import hillel.doctorRest.clinic.WisitHoursConfig;
import hillel.doctorRest.clinic.doctor.DoctorNotFoundException;
import hillel.doctorRest.clinic.doctor.DoctorService;
import hillel.doctorRest.clinic.pet.PetNotFoundException;
import hillel.doctorRest.clinic.pet.PetService;
import hillel.doctorRest.clinic.schedule.dto.*;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.val;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor

public class ScheduleController {
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final ScheduleRepository scheduleRepository;
    private final PetService petService;
    private WisitHoursConfig wisitHoursConfig;
    private final ScheduleDtoConverter scheduleDtoConverter;
    private final ScheduleModelConverter scheduleModelConverter;
    private final ScheduleMapOutputDtoConverter scheduleMapOutputDtoConverter;

    @GetMapping("/doctors/schedule")
    public List<ScheduleOutputDto>findAll(){
        return scheduleModelConverter.schedulesToOutputDto(scheduleService.findAll());
            }

    @GetMapping("/doctors/doctorId/schedule/visitDate")
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> findByDoctorIdAndVisitDate(@RequestParam Integer doctorId,
                                          @RequestParam ("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate) {
        if (doctorService.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException();
        } else return scheduleMapOutputDtoConverter
                .hourToPetId(scheduleModelConverter
                        .schedulesToOutputDto(scheduleService
                                .findByDoctorIdAndVisitDate(doctorId,visitDate)));
    }

    @PostMapping("/doctors/{doctorId}/schedule/{visitDate}/{hour}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createSchedule(@PathVariable Integer doctorId,
                               @RequestBody ScheduleInputDto scheduleInputDto,
                               @PathVariable ("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
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
