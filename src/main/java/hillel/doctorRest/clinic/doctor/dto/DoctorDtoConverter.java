package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface DoctorDtoConverter {
    @Mapping(target = "id", ignore = true)
    Doctor toModel(Integer degreeNumber, DoctorInputDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "degreeNumber", ignore = true)
    void update(@MappingTarget Doctor doctor, DoctorInputDtoForUpdate dtoForUpdate);
}

