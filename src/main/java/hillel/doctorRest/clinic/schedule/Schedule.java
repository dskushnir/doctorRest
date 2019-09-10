package hillel.doctorRest.clinic.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate visitDate;
    private Integer doctorId;
    private String hour;
    private String petId;

    public Schedule(LocalDate visitDate, Integer doctorId, String hour, String petId) {
        this.visitDate = visitDate;
        this.doctorId = doctorId;
        this.hour = hour;
        this.petId = petId;
    }
}


