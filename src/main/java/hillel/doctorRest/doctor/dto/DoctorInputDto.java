package hillel.doctorRest.doctor.dto;

import lombok.Data;

@Data
public class DoctorInputDto {
    private  Integer id;
    private  String name;
    private  String specialization;
}
