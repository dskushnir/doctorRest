package hillel.doctorRest.doctor;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class DoctorRepository {
    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();
    private final AtomicInteger counter = new AtomicInteger();

    public Doctor createDoctor(Doctor doctor) {
        Doctor created = doctor.toBuilder().id(counter.incrementAndGet()).build();
        doctors.add(created);
        return created;
    }

    public List<Doctor> findAll() {
        return doctors;
    }

    public java.util.Optional<Integer> findIndexById(Integer id) {
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



