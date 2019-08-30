package hillel.doctorRest.clinic.review.dto;

import hillel.doctorRest.clinic.review.Review;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Optional;


@Mapper
public interface ReviewModelConverter {
    ReviewOutputDto reviewToDto(Review review);
    List<ReviewOutputDto>reviewsToOutputDto(List<Review>reviews);
    default <T> T unpack(Optional<T> maybe){
        return maybe.orElse(null);
    }
}
