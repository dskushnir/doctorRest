package hillel.doctorRest.clinic.schedule;

import hillel.doctorRest.clinic.VisitHoursConfig;
import hillel.doctorRest.clinic.doctor.DoctorNotFoundException;
import hillel.doctorRest.clinic.doctor.DoctorService;
import hillel.doctorRest.clinic.pet.PetNotFoundException;
import hillel.doctorRest.clinic.pet.PetService;
import hillel.doctorRest.clinic.schedule.dto.*;
import lombok.AllArgsConstructor;

import org.hibernate.StaleObjectStateException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import lombok.val;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor


public class ScheduleController {
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;
    private final PetService petService;
    private VisitHoursConfig visitHoursConfig;
    private final ScheduleDtoConverter scheduleDtoConverter;
    private final ScheduleModelConverter scheduleModelConverter;


    @GetMapping("/doctors/{doctorId}/schedule")
    public List<Schedule> findAll(@PathVariable Integer doctorId){
        return scheduleService.findByDoctorId(doctorId);
    }

    @GetMapping("/doctors/{doctorId}/schedule/{visitDate}")
    public Map<String, Object> hourToPetId(@PathVariable Integer doctorId,
                                           @PathVariable("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate) {
        if (doctorService.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException();
        }
        Map<String, Object> map = new HashMap<>() ;

        val mapToPetId = (scheduleModelConverter
                .schedulesToOutputDto(scheduleService
                        .findByDoctorIdAndVisitDate(doctorId, visitDate)))
                .stream()
                .collect(Collectors
                        .toMap(ScheduleOutputDto::getHour, ScheduleOutputDto::getPetId));
        val sortedMapToPetId=mapToPetId.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        String methodName = new Object() {
        }
                .getClass()
                .getEnclosingMethod()
                .getName();
        map.put(methodName, sortedMapToPetId);
        return map;
    }

    @PostMapping("/doctors/{doctorId}/schedule/{visitDate}/{hour}")
    @ResponseStatus(HttpStatus.CREATED)
  //  @Retryable(StaleObjectStateException.class)
    public Schedule createSchedule(@PathVariable Integer doctorId,
                                   @RequestBody ScheduleInputDto scheduleInputDto,
                                   @PathVariable("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
                                   @PathVariable String hour) {
        if (doctorService.findById(doctorId).isEmpty()) {
            throw new DoctorNotFoundException();
        } else if (petService.findById(Integer.valueOf(scheduleInputDto.getPetId())).isEmpty()) {
            throw new PetNotFoundException();
        } else if (!visitHoursConfig.getHourName().contains(hour)) {
            throw new HourNotFoundException();
        } else if (!scheduleService.findByDoctorIdAndVisitDateAndHour(doctorId, visitDate, hour).isEmpty()) {
            throw new HourNotFoundAvailableException();
        } else {
            return scheduleService
                    .createSchedule(doctorId, visitDate, hour, scheduleDtoConverter
                            .toModel(scheduleInputDto));
        }
    }

    @PostMapping("/doctors/swap-doctors/{visitDate}/{doctor1Id}/{doctor2Id}")
    @ResponseStatus(HttpStatus.OK)
    public void swapOwners(@PathVariable("visitDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate visitDate,
                           @PathVariable Integer doctor1Id,
                           @PathVariable Integer doctor2Id) throws Exception {
        if (doctorService.findById(doctor1Id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Doctor with id="+doctor1Id+" not Found");
        }
        if (doctorService.findById(doctor2Id).isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Doctor with id="+doctor2Id+" not Found");
        }
        else scheduleService.swapListDoctors(visitDate, doctor1Id, doctor2Id);
    }
}

