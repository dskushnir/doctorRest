package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.SpecializationName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data

public class DoctorInputDtoForUpdate {
    @NotBlank
    private String name;
    @NotEmpty
    private List<@SpecializationName String> specializations;
}
