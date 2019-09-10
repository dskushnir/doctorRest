package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface DoctorDtoConverter {
    @Mapping(target = "id", ignore = true)
    Doctor toModel(DoctorInputDto dto);
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Doctor doctor, DoctorInputDto dto);

}

