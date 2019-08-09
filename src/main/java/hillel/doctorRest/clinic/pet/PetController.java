package hillel.doctorRest.clinic.pet;

import hillel.doctorRest.clinic.doctor.DoctorNotFoundException;
import hillel.doctorRest.clinic.pet.dto.PetDtoConverter;
import hillel.doctorRest.clinic.pet.dto.PetInputDto;
import hillel.doctorRest.clinic.pet.dto.PetModelConverter;
import hillel.doctorRest.clinic.pet.dto.PetOutputDto;
import lombok.AllArgsConstructor;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor

public class PetController {
    private final PetService petService;
    private final PetDtoConverter petDtoConverter;
    private final PetModelConverter petModelConverter;

    @GetMapping("/pets")
    public List<PetOutputDto> getAll() {
        val pets=petService.findAll();
        if (pets.size() == 0) {
            throw new PetNotFoundException();
        }
        return pets.stream()
                .map(pet -> petModelConverter.toDto(pet))
                .collect(Collectors.toList());
    }

    @PostMapping("/pets")
    @ResponseStatus (HttpStatus.CREATED)
    public void create(@RequestBody PetInputDto petInputDto) {
        petService.createPet(petDtoConverter.toModel(petInputDto));
    }
}