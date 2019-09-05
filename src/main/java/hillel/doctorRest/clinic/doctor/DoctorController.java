package hillel.doctorRest.clinic.doctor;

import hillel.doctorRest.clinic.doctor.dto.DoctorDtoConverter;
import hillel.doctorRest.clinic.doctor.dto.DoctorInputDto;
import hillel.doctorRest.clinic.doctor.dto.DoctorModelConverter;
import hillel.doctorRest.clinic.doctor.dto.DoctorOutputDto;


import lombok.val;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController

public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorModelConverter doctorModelConverter;
    private final UriComponentsBuilder uriBuilder;

    public DoctorController(DoctorService doctorService, DoctorDtoConverter doctorDtoConverter,
                            DoctorModelConverter doctorModelConverter,
                            @Value("${clinic.host-name:localhost}") String hostName) {
        this.doctorService = doctorService;
        this.doctorDtoConverter = doctorDtoConverter;
        this.doctorModelConverter = doctorModelConverter;
        uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostName)
                .path("/doctors/{id}");
    }

    @GetMapping("/doctors/{id}")
    public DoctorOutputDto findById(@PathVariable Integer id) {
        val mayBeDoctor = doctorService.findById(id);
        val doctor = mayBeDoctor.orElseThrow(DoctorNotFoundException::new);
        val dtoDoctor = doctorModelConverter.toDto(doctor);
        return dtoDoctor;
    }

    @GetMapping("/doctors")
    public Page<DoctorOutputDto> findAll(
            @RequestParam Optional<String> name,
            @RequestParam Optional<List<String>> specializations, Pageable pageable) {
        val doctors = doctorService.findAll(name, specializations, pageable);
        if (doctors.getTotalElements() == 0) {
            throw new DoctorNotFoundException();
        }
        return doctors
                .map(doctor -> doctorModelConverter.toDto(doctor));
    }

    @PostMapping("/doctors")
    public ResponseEntity<Object> createDoctor(@Valid @RequestBody DoctorInputDto dto) {
        val created = doctorService.createDoctor(doctorDtoConverter.toModel(dto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody DoctorInputDto dto,
                                          @PathVariable Integer id) {
        var doctor1 = doctorService.findById(id).orElseThrow(DoctorNotFoundException::new);
        doctorDtoConverter.update(doctor1, dto);
        doctorService.save(doctor1);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
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



















