package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.apache.el.stream.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor


public class DoctorController {
    private final DoctorService doctorService;


    @GetMapping("/doctors/{id}")
    public Doctor findById(@PathVariable Integer id) {
        var mayBeDoctor = doctorService.findById(id);
        return mayBeDoctor.orElseThrow(DoctorNotFoundException::new);

    }

    @GetMapping("/doctors")
    public List<Doctor> findAll() {
        return doctorService.findAll();
    }



   @GetMapping  (value = "/doctors",params = {"specialization=surgeon"})//(value = "/doctors",params = {"specialization=surgeon"})
    public List<Doctor> findBySpecialization(@RequestParam("specialization")String specialization)  {

        return doctorService.findBySpecialization(specialization);
    }

   @GetMapping  ( "/doctors")

    public  List<Doctor> findByLetter(@RequestParam (name = "A.+")String name) {

        return doctorService.findByFirstLetterName(name);
    }





@PostMapping("/doctors")
    public ResponseEntity<Object> createDoctor(@RequestBody Doctor doctor) {

    try {
        doctorService.saveDoctor(doctor);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("doctors//{id}")
                .buildAndExpand(doctor.getId()).toUri();
        return ResponseEntity.created(location).body(location);
    } catch (IdPredeterminedException e) {
        return ResponseEntity.badRequest().build();
    }
}










    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody Doctor doctor,
                                       @PathVariable Integer id) {
        if (!doctor.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            doctorService.update(doctor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?>deleteDoctor(@PathVariable Integer id){
        try {
            doctorService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }catch  (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}

