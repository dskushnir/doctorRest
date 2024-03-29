package hillel.doctorRest.clinic.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findByDoctorIdAndVisitDateAndHour(Integer doctorId, LocalDate visitDate, String hour);

    List<Schedule> findByDoctorIdAndVisitDate(Integer doctorId, LocalDate visitDate);

    List<Schedule> findByDoctorId(Integer doctorId);
}
