package hillel.doctorRest.doctor;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class DoctorRepository {
    private final Map<Integer, Doctor> idToDoctor = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger();


    public List<Doctor> findAll() {
        return new ArrayList<>(idToDoctor.values());
    }

    public Doctor create(Doctor doctor) {
        Doctor created = doctor.toBuilder().id(counter.incrementAndGet()).build();
        idToDoctor.put(created.getId(), created);
        return created;
    }


    public Optional<Doctor> findById(Integer id) {
        return Optional.ofNullable(idToDoctor.get(id));
    }

    public void update(Doctor doctor) {
        idToDoctor.replace(doctor.getId(), doctor);
    }


    public void delete(Integer id) {
        idToDoctor.remove(id);

    }

    public void deleteAll() {
        idToDoctor.clear();
    }
}





