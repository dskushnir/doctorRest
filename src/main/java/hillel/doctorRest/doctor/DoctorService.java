package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DoctorService {
    private final DoctorRepository doctorRepository;

    private Predicate<Doctor> filterById(Integer id) {
        return doctor -> doctor.getId().equals(id);
    }

    private Predicate<Doctor> filterByFirstLatterName(String letter) {
        return doctor -> doctor.getName().startsWith(letter);
    }

    private Predicate<Doctor> filterDyName(String name) {
        return doctor -> doctor.getName().equalsIgnoreCase(name);
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }

    public List<Doctor> findAll(Predicate<Doctor> predicate) {
        return doctorRepository.findAll().stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public Predicate<Doctor> predicate(java.util.Optional<Integer> id, java.util.Optional<String> nameLetter,
                                       java.util.Optional<String> name, java.util.Optional<String> specialization) {
        java.util.Optional<Predicate<Doctor>> maybeIdPredicate = id
                .map(this::filterById);
        java.util.Optional<Predicate<Doctor>> maybeNamePredicate = name
                .map(this::filterDyName);
        java.util.Optional<Predicate<Doctor>> maybeByFirstLatterNamePredicate = nameLetter
                .map(this::filterByFirstLatterName);
        java.util.Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization
                .map(this::filterBySpecialization);
        return Stream.of(maybeIdPredicate, maybeNamePredicate, maybeByFirstLatterNamePredicate, maybeSpecializationPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.create(doctor);
    }

    public void update(Doctor doctor) {
        doctorRepository.update(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.delete(id);
    }
}
