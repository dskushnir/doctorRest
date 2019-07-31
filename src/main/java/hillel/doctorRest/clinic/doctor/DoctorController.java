package hillel.doctorRest.clinic.doctor;

import hillel.doctorRest.clinic.SpecializationConfig;
import hillel.doctorRest.clinic.doctor.dto.DoctorDtoConverter;
import hillel.doctorRest.clinic.doctor.dto.DoctorInputDto;
import hillel.doctorRest.clinic.doctor.dto.DoctorModelConverter;
import hillel.doctorRest.clinic.doctor.dto.DoctorOutputDto;
import hillel.doctorRest.clinic.info.InfoController;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController

public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorModelConverter doctorModelConverter;
    private final UriComponentsBuilder uriBuilder;
    private SpecializationConfig specializationConfig;


    public DoctorController(DoctorService doctorService, DoctorDtoConverter doctorDtoConverter,
                            DoctorModelConverter doctorModelConverter, SpecializationConfig specializationConfig,
                            @Value("${clinic.host-name:localhost}") String hostName) {
        this.doctorService = doctorService;
        this.doctorDtoConverter = doctorDtoConverter;
        this.doctorModelConverter = doctorModelConverter;
        this.specializationConfig = specializationConfig;
        uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostName)
                .path("/doctors/{id}");
    }

    @GetMapping("/doctors")
    public List<DoctorOutputDto> findAll(Optional<Integer> id,
                                         java.util.Optional<String> nameLetter,
                                         java.util.Optional<String> name,
                                         java.util.Optional<String> specialization) {
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
        if (specializationConfig.getSpecializationName().contains(dto.getSpecialization())) {
            val created = doctorService.createDoctor(doctorDtoConverter.toModel(dto));
            return ResponseEntity.created(uriBuilder.build(created.getId())).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@RequestBody DoctorInputDto dto,
                                          @PathVariable Integer id) {
        if (!specializationConfig.getSpecializationName().contains(dto.getSpecialization())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            val doctor = doctorDtoConverter.toModel(dto, id);
            doctorService.update(doctor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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


















