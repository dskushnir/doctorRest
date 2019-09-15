package hillel.doctorRest.clinic.degree.dto;

import hillel.doctorRest.clinic.degree.Degree;
import org.mapstruct.Mapper;

@Mapper
public interface DegreeModelConverter {
    DegreeOutputDto toDegreeDto(Degree degree);
}
