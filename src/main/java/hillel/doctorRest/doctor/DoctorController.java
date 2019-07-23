package hillel.doctorRest.doctor;

import hillel.doctorRest.doctor.dto.DoctorDtoConverter;
import hillel.doctorRest.doctor.dto.DoctorInputDto;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/pets/{id}");

    @GetMapping("/doctors")
    public List<Doctor> findAll(java.util.Optional<String> name,
                                java.util.Optional<String> specialization) {
        java.util.Optional<Predicate<Doctor>> maybeNamePredicate = name
                .map(this::filterByName);
        java.util.Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization
                .map(this::filterBySpecialization);
        Predicate<Doctor> predicate = Stream.of(maybeNamePredicate, maybeSpecializationPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
        return doctorService.findAll(predicate);
    }

    private Predicate<Doctor> filterByName(String letter) {
        return doctor -> doctor.getName().startsWith(letter);
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }

    @GetMapping("/doctors/{id}")
    public Doctor findById(@PathVariable Integer id) {
        val mayBeDoctor = doctorService.findById(id);
        return mayBeDoctor.orElseThrow(DoctorNotFoundException::new);
    }

    @PostMapping("/doctors")
    public ResponseEntity<Object> createDoctor(@RequestBody DoctorInputDto dto) {
        try {
          val created =doctorService.createDoctor(doctorDtoConverter.toModel(dto));
            return ResponseEntity.created(uriBuilder.build(created.getId())).build();
        } catch (IdPredeterminedException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto dto,
                                          @PathVariable Integer id) {
        if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        try {
            val doctor = doctorDtoConverter.toModel(dto, id);
            doctorService.update(doctor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {
        try {
            doctorService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}


















