package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class DoctorService {
    private final DoctorRepository doctorRepository;
    private final IdGenerator idGenerator=new IdGenerator();


    public Doctor createDoctor(String name, String specialization){
       return new Doctor(idGenerator.generateId(),  name, specialization);
      //  doctorRepository.saveDoctor(doctor1);
       /// return doctor;

    }
    public void saveDoctor(Doctor doctor){
        doctorRepository.saveDoctor(createDoctor(doctor.getName(),doctor.getSpecialization()));

    }



    public List<Doctor>findBySpecialization(String specialization){
        return doctorRepository.findBySpecialization(specialization);
    }


   public List<Doctor>findByFirstLetterName(String name) {
        return doctorRepository.findByFirstLetterName(name);
    }



    public Optional<Doctor> findById(Integer id) {
        return doctorRepository.findById(id);
    }

    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }

    public void update(Doctor doctor) {
        doctorRepository.update(doctor);
    }

    public void delete(Integer id) {
        doctorRepository.delete(id);
    }
}
