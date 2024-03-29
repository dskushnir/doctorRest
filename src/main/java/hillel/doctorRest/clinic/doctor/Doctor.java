package hillel.doctorRest.clinic.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)

@Entity
public class Doctor {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String>specializations;
    private Integer degreeNumber;

    public Doctor(String name, List<String> specializations,Integer degreeNumber) {
        this.name = name;
        this.specializations = specializations;
        this.degreeNumber =degreeNumber;
    }
}

