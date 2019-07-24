package hillel.doctorRest.doctor.dto;

import hillel.doctorRest.doctor.Doctor;
import lombok.Data;


@Data
public class DoctorOutputDto {
    private Integer id;
    private String name;
    private String Specialisation;

    private DoctorOutputDto createDoctorDTO( Doctor doctor) {
        DoctorOutputDto doctorOutputDto = new DoctorOutputDto();
        doctorOutputDto.setId(doctor.getId());
        doctorOutputDto.setName(doctor.getName());
        doctorOutputDto.setSpecialisation(doctor.getSpecialization());
        return doctorOutputDto;
    }
}
