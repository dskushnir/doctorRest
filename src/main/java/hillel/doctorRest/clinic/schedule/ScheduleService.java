package hillel.doctorRest.clinic.schedule;

import hillel.doctorRest.clinic.review.VisitNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.val;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    public Schedule createSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
    public Optional<Schedule> findById (Integer scheduleId){
        return scheduleRepository.findById(scheduleId);
    }
    public LocalDateTime dateTimeSchedule(Integer scheduleId){
        if (scheduleRepository.findById(scheduleId).isPresent()){
            return LocalDateTime.of(scheduleRepository.findById(scheduleId).get().getVisitDate(),
                    LocalTime.of(Integer.valueOf(findById(scheduleId).get().getHour()),0,0,0));
        }
        throw new VisitNotFoundException();
    }

    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    public List<Schedule> findByDoctorId(Integer id) {
        return scheduleRepository.findByDoctorId(id);
    }

    public List<Schedule> findByDoctorIdAndVisitDate(Integer id,
                                                     LocalDate visitDate) {
        return scheduleRepository.findByDoctorIdAndVisitDate(id, visitDate);
    }

    public Optional<Schedule> findByDoctorIdAndVisitDateAndHour(Integer doctorId,
                                                                LocalDate visitDate,
                                                                String hour) {
        return scheduleRepository.findByDoctorIdAndVisitDateAndHour(doctorId, visitDate, hour);
    }
    @Transactional(rollbackFor = Exception.class)
    public void swapListDoctors(LocalDate visitDate, Integer doctor1Id, Integer doctor2Id) throws Exception {
        val scheduleDoctor1 = findByDoctorIdAndVisitDate(doctor1Id, visitDate);
        val scheduleDoctor2 = findByDoctorIdAndVisitDate(doctor2Id, visitDate);
        val hoursDoctor1 = scheduleDoctor1.stream().map(Schedule::getHour).collect(Collectors.toList());
        val hoursDoctor2 = scheduleDoctor2.stream().map(Schedule::getHour).collect(Collectors.toList());
        if (hoursDoctor1.isEmpty()) {
            throw new DoctorNotFoundSсheduleException();
        }
        if (hoursDoctor2.containsAll(hoursDoctor1)) {
            throw new DoctorIsBusyException();
        }
        scheduleDoctor1.forEach(schedule -> schedule.setDoctorId(doctor2Id));
        scheduleDoctor1.forEach(schedule -> saveSchedule(schedule));
    }

    public Schedule saveSchedule(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }
}
