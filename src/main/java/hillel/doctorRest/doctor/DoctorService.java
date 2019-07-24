package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public List<Doctor> findAll(Predicate<Doctor> predicate) {
        return doctorRepository.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }


  /* public Optional<Doctor> findById(Integer id) {
        return doctorRepository.findById(id);
    }*/
    public Doctor createDoctor (Doctor doctor) {
        return doctorRepository.create(doctor);
    }

    public void update(Doctor doctor) {
        doctorRepository.update(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.delete(id);
    }
}
