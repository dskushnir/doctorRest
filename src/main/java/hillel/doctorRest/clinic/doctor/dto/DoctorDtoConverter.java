package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DoctorDtoConverter {
    @Mapping(target = "id", ignore = true)
    Doctor toModel(DoctorInputDto dto);

    Doctor toModel(DoctorInputDto dto, Integer id);

}

