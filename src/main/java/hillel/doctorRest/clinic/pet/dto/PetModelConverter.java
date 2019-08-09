package hillel.doctorRest.clinic.pet.dto;

import hillel.doctorRest.clinic.pet.Pet;
import org.mapstruct.Mapper;

@Mapper

public interface PetModelConverter {
    PetOutputDto toDto (Pet pet);
}
