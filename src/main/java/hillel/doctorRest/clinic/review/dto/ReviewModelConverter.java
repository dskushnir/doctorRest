package hillel.doctorRest.clinic.review.dto;

import hillel.doctorRest.clinic.review.Review;
import hillel.doctorRest.clinic.review.ReviewService;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;


@Mapper
public interface ReviewModelConverter {
    ReviewOutputDto reviewToDto(Review review);

    List<ReviewOutputDto> reviewsToOutputDto(List<Review> reviews);

    default <T> T unpack(Optional<T> maybe) {

        return maybe.orElse(null);
    }

    ReportOutputDto reportToDto(ReviewService.Report report);
}
