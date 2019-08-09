package hillel.doctorRest.clinic.pet.dto;

import hillel.doctorRest.clinic.pet.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PetDtoConverter {
    @Mapping(target = "id", ignore = true)
    Pet toModel(PetInputDto petInputDto);
    Pet toModel(PetInputDto petInputDto,Integer id);
}
