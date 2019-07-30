package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.Doctor;
import org.mapstruct.Mapper;

@Mapper
public interface DoctorModelConverter {
    DoctorOutputDto toDto(Doctor doctor);
}
