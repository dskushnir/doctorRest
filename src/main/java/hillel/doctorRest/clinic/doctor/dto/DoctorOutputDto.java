package hillel.doctorRest.clinic.doctor.dto;

        import lombok.Data;

        import java.util.List;

@Data
public class DoctorOutputDto {
    private Integer id;
    private String name;
    private List<String> specializations;
    private Integer degreeNumber;
}
