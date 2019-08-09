package hillel.doctorRest.clinic.schedule;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public List<Schedule>findByDoctorIdAndVisitDate(Integer id,
                                                    LocalDateTime visitDate){
        return scheduleRepository.findByDoctorIdAndVisitDate(id,visitDate);
    }

    public Optional<Schedule> findByHour(String hour) {
        return scheduleRepository.findByHour(hour);
    }

    public void createSchedule(Integer doctorId,
                               LocalDateTime visitDate,
                               String hour, Schedule schedule) {

        schedule.setDoctorId(doctorId);
        schedule.setVisitDate(visitDate);
        schedule.setHour(hour);
        scheduleRepository.save(schedule);
    }
}
