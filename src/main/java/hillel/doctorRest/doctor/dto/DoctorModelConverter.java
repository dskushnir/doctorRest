package hillel.doctorRest.doctor.dto;

import hillel.doctorRest.doctor.Doctor;
import org.mapstruct.Mapper;

@Mapper
public interface DoctorModelConverter {
    DoctorOutputDto toDto(Doctor doctor);
}
