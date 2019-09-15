package hillel.doctorRest.clinic.degree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@Data

@AllArgsConstructor
@NoArgsConstructor

@Builder(toBuilder = true)
@Entity
public class Degree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer degreeNumber;
    private String indicationEducationalEstablishment;
    private String specialty;
    private LocalDate dateCompletionOfStudy;
}
