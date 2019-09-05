package hillel.doctorRest.clinic.doctor;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    public Optional<Doctor> findById(Integer id) {
        return doctorRepository.findById(id);
    }

    public Page<Doctor> findAll(Optional<String> name,
                                Optional<List<String>> specializations, Pageable pageable) {
        if (name.isPresent() && specializations.isPresent()) {
            return doctorRepository.findByParameter(name.get(), specializations.get(), pageable);
        }
        if (name.isPresent()) {
            return doctorRepository.findByNameIgnoreCase(name.get(), pageable);
        }
        if (specializations.isPresent()) {
            return doctorRepository.findBySpecializationsIn(specializations.get(), pageable);
        }
        return doctorRepository.findAll(pageable);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.deleteById(id);
    }
}

