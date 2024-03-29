package hillel.doctorRest.clinic.review.dto;

import hillel.doctorRest.clinic.review.Review;
import org.mapstruct.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ReviewDtoConverter {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Review toModel(Integer scheduleId,
                   ReviewInputDto reviewInputDto,
                   LocalDateTime localDateTimeReview);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "localDateTimeReview", ignore = true)
    Review toModel(ReviewInputForUpdateDto reviewInputForUpdateDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "scheduleId", ignore = true)
    @Mapping(target = "localDateTimeReview", ignore = true)
    void update(@MappingTarget Review review, ReviewInputForUpdateDto reviewInputForUpdateDto);

    default <T> T unpack(Optional<T> maybe) {
        return maybe.orElse(null);
    }
}
