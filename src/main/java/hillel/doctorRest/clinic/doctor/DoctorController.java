package hillel.doctorRest.clinic.doctor;

import hillel.doctorRest.clinic.degree.DegreeNotFoundException;
import hillel.doctorRest.clinic.degree.DegreeService;
import hillel.doctorRest.clinic.degree.dto.DegreeDtoConverter;
import hillel.doctorRest.clinic.degree.dto.DegreeInputDto;
import hillel.doctorRest.clinic.degree.dto.DegreeModelConverter;
import hillel.doctorRest.clinic.degree.dto.DegreeOutputDto;
import hillel.doctorRest.clinic.doctor.dto.*;
import hillel.doctorRest.clinic.info.DegreeServiceConfig;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j

public class DoctorController {
    private final DoctorService doctorService;
    private final DoctorDtoConverter doctorDtoConverter;
    private final DoctorModelConverter doctorModelConverter;
    private final UriComponentsBuilder uriBuilder;
    private final RestTemplate restTemplate;
    private final DegreeService degreeService;
    private final DegreeDtoConverter degreeDtoConverter;
    private final DegreeModelConverter degreeModelConverter;
    private final DegreeServiceConfig degreeServiceConfig;


    public DoctorController(DoctorService doctorService, DoctorDtoConverter doctorDtoConverter,
                            DoctorModelConverter doctorModelConverter,
                            @Value("${clinic.host-name:localhost}") String hostName,
                            DegreeService degreeService, DegreeDtoConverter degreeDtoConverter,
                            DegreeModelConverter degreeModelConverter, DegreeServiceConfig degreeServiceConfig) {
        this.doctorService = doctorService;
        this.doctorDtoConverter = doctorDtoConverter;
        this.doctorModelConverter = doctorModelConverter;
        uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostName)
                .path("/doctors/{id}");
        this.restTemplate = new RestTemplate();
        this.degreeService = degreeService;
        this.degreeDtoConverter = degreeDtoConverter;
        this.degreeModelConverter = degreeModelConverter;
        this.degreeServiceConfig = degreeServiceConfig;
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

    @GetMapping("/degrees/{degreeNumber}")
    @ResponseStatus(HttpStatus.OK)
    public DegreeOutputDto findByDegreeNumber(@PathVariable Integer degreeNumber) {
        val mayBeDegree = degreeService.findByDegreeNumber(degreeNumber);
        return degreeModelConverter.toDegreeDto(mayBeDegree.orElseThrow(DegreeNotFoundException::new));

    }

    @PostMapping("/doctors/{degreeNumber}")
    public ResponseEntity<Object> createDoctor(@NotNull @PathVariable Integer degreeNumber, @Valid @RequestBody DoctorInputDto doctorInputDto) {
        try {
            log.info("Calling degree service");
            log.debug("Degree service URL: {}", degreeServiceConfig.getDegreeUrl());
            val degreeDto = restTemplate.getForObject(degreeServiceConfig.getDegreeUrl() + "/degrees/" + degreeNumber.toString(), DegreeInputDto.class);
            log.info("Degree service responded ok");
            log.debug("Degree  service response: {}", degreeDto);
            val degree = degreeDtoConverter.toModelDegree(degreeDto);
            log.info("DegreeDtoConverter convert degreeDto to model");
            log.info("Degree service create degree");
            degreeService.createDegree(degree);
            log.debug("Degree repository save : {}", degree);
            log.info("DoctorDtoConverter convert degreeNumber witch doctorInputDto to model doctor ");
            log.info("Doctor service created doctor ");
            val created = doctorService.createDoctor(doctorDtoConverter.toModel(degreeNumber, doctorInputDto));
            log.debug("Doctor service create doctor: {}", created);
            return ResponseEntity.created(uriBuilder.build(created.getId())).build();
        } catch (Exception e) {
            log.error("Call  to degree service finished  exceptionally", e);
           return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<?> updateDoctor(@Valid @RequestBody DoctorInputDtoForUpdate dtoForUpdate,
                                          @PathVariable Integer id) {
        var doctor1 = doctorService.findById(id).orElseThrow(DoctorNotFoundException::new);
        doctorDtoConverter.update(doctor1, dtoForUpdate);
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



















