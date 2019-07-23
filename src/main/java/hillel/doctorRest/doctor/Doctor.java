package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)


public class Doctor {

    private  Integer id;
    private  String name;
    private  String specialization;
}

