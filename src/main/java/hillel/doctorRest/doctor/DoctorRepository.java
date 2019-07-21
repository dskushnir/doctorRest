package hillel.doctorRest.doctor;


import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Repository

public class DoctorRepository {
    private final List<Doctor> doctors = new CopyOnWriteArrayList<>();

    {
    }


    private java.util.Optional<Integer> findIndexById(Integer id) {
        for (int i = 0; i < doctors.size(); i++) {
            if (doctors.get(i).getId().equals(id)) {
                return java.util.Optional.of(i);
            }
        }
        return Optional.empty();
    }

    public void saveDoctor(Doctor doctor) {
        findIndexById(doctor.getId()).ifPresentOrElse(idx ->
        {
            throw new IdPredeterminedException();
        }, () -> doctors.add(doctor))


        ;
    }

    public Optional<Doctor> findById(Integer id) {
        return doctors.stream()
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    public List<Doctor> findBySpecialization(String specialization) {
        return doctors.stream().filter(it -> it.getSpecialization().
                equalsIgnoreCase(specialization)).
                collect(Collectors.toList());

    }
   /* public List<Doctor>findByFirstLetterName(char ch){
        return doctors.stream().filter(it->it.getName().
                charAt(0)==ch).collect(Collectors.toList());
    }*/

    public List<Doctor> findByFirstLetterName(String name) {
        return doctors.stream().filter(it -> it.getName().
                equalsIgnoreCase(name)).
                collect(Collectors.toList());
    }


    public List<Doctor> findAll() {
        return doctors;
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
