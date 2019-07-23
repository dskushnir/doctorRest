package hillel.doctorRest.doctor;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class DoctorRepository {
    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();

    public void saveDoctor(Doctor doctor) {
        findIndexById(doctor.getId()).ifPresentOrElse(idx -> {
            throw new IdPredeterminedException();
        }, () -> doctors.add(doctor));
    }

    public List<Doctor> findAll() {
        return doctors;
    }

    private java.util.Optional<Integer> findIndexById(Integer id) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(id)) {
                return java.util.Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public Optional<Doctor> findById(Integer id) {
        return doctors.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    public void update(Doctor doctor) {

        findIndexById(doctor.getId()).
                ifPresentOrElse(idx -> doctors.set(idx, doctor), () -> {
                    throw new NoSuchDoctorException();
                });
    }

    public void delete(Integer id) {
        findIndexById(id).ifPresentOrElse(idx -> doctors.remove(idx.intValue()), () -> {
            throw new NoSuchDoctorException();
        });
    }
}



