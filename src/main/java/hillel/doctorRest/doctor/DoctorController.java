package hillel.doctorRest.doctor;

import hillel.doctorRest.doctor.dto.DoctorDtoConverter;
import hillel.doctorRest.doctor.dto.DoctorInputDto;
import hillel.doctorRest.doctor.dto.DoctorOutputDto;
import lombok.AllArgsConstructor;
import lombok.val;
import org.mapstruct.ap.internal.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    @GetMapping("/doctors")
    public List<Doctor> findAll(java.util.Optional<String> name,
                                java.util.Optional<String> specialization) {
        java.util.Optional<Predicate<Doctor>> maybeByFirstLatterNamePredicate = name
                .map(this::filterByFirstLatterName);
        java.util.Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization
                .map(this::filterBySpecialization);
        Predicate<Doctor> predicate = Stream.of(maybeByFirstLatterNamePredicate, maybeSpecializationPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
        return doctorService.findAll(predicate);
    }

    private Predicate<Doctor> filterByFirstLatterName(String letter) {
        return doctor -> doctor.getName().startsWith(letter);
    }

    private Predicate<Doctor> filterBySpecialization(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }
    private Predicate<Doctor>filterDyName(String name){
        return doctor -> doctor.getName().equalsIgnoreCase(name);

    }
    private Predicate<Doctor>filterById(Integer id){
        return doctor -> doctor.getId().equals(id);
    }

    @GetMapping("/doctors")
    public List<DoctorOutputDto> findParameter(DoctorOutputDto outputDto, Optional<Integer>id, Optional<String>name, Optional<String>specialization) {

        java.util.Optional<Predicate<Doctor>> maybeIdPredicate = id
                .map(this::filterById);
        java.util.Optional<Predicate<Doctor>> maybeNamePredicate = name
                .map(this::filterDyName);
        java.util.Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization
                .map(this::filterBySpecialization);
        Predicate<Doctor> predicate = Stream.of(maybeIdPredicate, maybeNamePredicate, maybeSpecializationPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
        List<Doctor> doctors = doctorService.findAll(predicate);
        val doctorDto = doctorDtoConverter.toDto(outputDto);
        return doctors.stream()
                .map(doc ->doctorDto).collect(Collectors.toList());
                /*  private PostDto convertToDto(Post post) {
        PostDto postDto = modelMapper.map(post, PostDto.class);
        postDto.setSubmissionDate(post.getSubmissionDate(),
                userService.getCurrentUser().getPreference().getTimezone());
        return postDto;
    }


   /* @GetMapping("/doctors")
    public Doctor findByParametr(Optional<Integer>id, Optional<String>name, Optional<String>specialization){

        java.util.Optional<Predicate<Doctor>> maybeIdPredicate = id
                .map(this::filterById);

        java.util.Optional<Predicate<Doctor>> maybeNamePredicate = name
                .map(this::filterDyName);
        java.util.Optional<Predicate<Doctor>> maybeSpecializationPredicate = specialization
                .map(this::filterBySpecialization);
        Predicate<Doctor> predicate = Stream.of(maybeIdPredicate, maybeNamePredicate, maybeSpecializationPredicate)
                .flatMap(Optional::stream)
                .reduce(Predicate::and)
                .orElse(doctor -> true);
        return doctorService.findByPredicate(predicate);
    }*/



  /*  @GetMapping("/doctors/{id}")
    public Doctor findById(@PathVariable Integer id) {
        val mayBeDoctor = doctorService.findById(id);
        return mayBeDoctor.orElseThrow(DoctorNotFoundException::new);
    }*/


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
      /*  if (!dto.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }*/
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


















