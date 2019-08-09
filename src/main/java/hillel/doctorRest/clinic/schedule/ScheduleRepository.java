package hillel.doctorRest.clinic.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByHour(String hour);
    List<Schedule>findByDoctorIdAndVisitDate(Integer doctorId, LocalDateTime visitDate);
}
