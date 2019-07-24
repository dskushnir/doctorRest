package hillel.doctorRest.doctor.dto;

import hillel.doctorRest.doctor.Doctor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DoctorDtoConverter {
    @Mapping(target = "id", ignore = true)
    Doctor toModel(DoctorInputDto dto);
    Doctor toModel(DoctorInputDto dto, Integer id);

    Doctor toDto(DoctorOutputDto outputDto);



}
