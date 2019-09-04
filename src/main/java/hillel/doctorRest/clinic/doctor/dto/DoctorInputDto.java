package hillel.doctorRest.clinic.doctor.dto;

import hillel.doctorRest.clinic.doctor.SpecializationName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class DoctorInputDto {
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @Size(min = 1)
    private List<@SpecializationName String> specializations;
}


