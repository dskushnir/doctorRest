package hillel.doctorRest.clinic.review.dto;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ReportDtoConverter {
    List< Object> toDtoReportReview(List<Object> reportReview);
}
