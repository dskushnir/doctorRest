package hillel.doctorRest.clinic.pet;

import hillel.doctorRest.clinic.pet.dto.PetDtoConverter;
import hillel.doctorRest.clinic.pet.dto.PetInputDto;
import hillel.doctorRest.clinic.pet.dto.PetModelConverter;
import hillel.doctorRest.clinic.pet.dto.PetOutputDto;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PetController {
    private final PetService petService;
    private final PetDtoConverter petDtoConverter;
    private final PetModelConverter petModelConverter;
    private final UriComponentsBuilder uriBuilder;

    public PetController(PetService petService,
                         PetDtoConverter petDtoConverter,
                         PetModelConverter petModelConverter,
                         @Value("${clinic.host-name:localhost}") String hostName) {
        this.petService = petService;
        this.petDtoConverter = petDtoConverter;
        this.petModelConverter = petModelConverter;
        this.uriBuilder = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host(hostName)
                .path("/pets/{id}");
    }

    @GetMapping("/pets")
    public List<PetOutputDto> getAll() {
        val pets = petService.findAll();
        if (pets.size() == 0) {
            throw new PetNotFoundException();
        }
        return pets.stream()
                .map(pet -> petModelConverter.toDto(pet))
                .collect(Collectors.toList());
    }

    @PostMapping("/pets")
    public ResponseEntity<Object> createPet(@RequestBody PetInputDto petInputDto) {
        val created = petService.createPet(petDtoConverter.toModel(petInputDto));
        return ResponseEntity.created(uriBuilder.build(created.getId())).build();
    }
}