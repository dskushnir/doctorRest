package hillel.doctorRest.doctor;

import hillel.doctorRest.doctor.dto.DoctorDtoConverter;
import hillel.doctorRest.doctor.dto.DoctorInputDto;
import hillel.doctorRest.doctor.dto.DoctorModelConverter;
import hillel.doctorRest.doctor.dto.DoctorOutputDto;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorModelConverter doctorModelConverter;
    private final UriComponentsBuilder uriBuilder = UriComponentsBuilder.newInstance()
            .scheme("http")
            .host("localhost")
            .path("/doctors/{id}");

    @GetMapping("/doctors")
    public List<DoctorOutputDto> findAll(Optional<Integer> id, java.util.Optional<String> nameLetter,
                                               java.util.Optional<String> name, java.util.Optional<String> specialization) {
        val doctors = doctorService.findAll(doctorService.predicate(id, nameLetter, name, specialization));
        if (doctors.size() == 0) {
            throw new DoctorNotFoundException();
        }
        return doctors.stream()
                .map(doc -> doctorModelConverter.toDto(doc))
                .collect(Collectors.toList());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Object> createDoctor(@RequestBody DoctorInputDto dto) {
        val created = doctorService.createDoctor(doctorDtoConverter.toModel(dto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto dto,
                                          @PathVariable Integer id) {
        val doctor = doctorDtoConverter.toModel(dto, id);
        try {
            doctorService.update(doctor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@RequestBody DoctorInputDto dto,
                                          @PathVariable Integer id) {
        val doctor = doctorDtoConverter.toModel(dto, id);
        try {
            doctorService.delete(doctor.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (NoSuchDoctorException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


















