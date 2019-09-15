package hillel.doctorRest.clinic.degree.dto;

import hillel.doctorRest.clinic.degree.Degree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface DegreeDtoConverter {
    @Mapping(target = "id", ignore = true)
    Degree toModelDegree(DegreeInputDto degreeInputDto);
}
