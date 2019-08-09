package hillel.doctorRest.clinic.schedule;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDateTime visitDate;
    private Integer doctorId;
    private String hour;
    private String petId;
}

  /*  @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name = "hour")
    @Column(name = "petId")
    private Map<String,String>hourToPetId=new HashMap<>();
*/

