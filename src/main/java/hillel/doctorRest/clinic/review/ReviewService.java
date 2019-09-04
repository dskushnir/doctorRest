package hillel.doctorRest.clinic.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }

    public void saveReview(Review review) {
        reviewRepository.save(review);
    }

    public Optional<Review> findById(Integer id) {
        return reviewRepository.findById(id);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public double averageService() {
        return findAll().stream().map(review -> review.getService())
                .flatMap(Optional::stream)
                .mapToDouble(value -> value)
                .average().getAsDouble();
    }

    public double averageEquipment() {
        return findAll().stream().map(review -> review.getEquipment())
                .flatMap(Optional::stream)
                .mapToDouble(value -> value)
                .average().getAsDouble();
    }

    public double averageQualificationSpecialist() {
        return findAll().stream().map(review -> review.getQualificationSpecialist())
                .flatMap(Optional::stream)
                .mapToDouble(value -> value)
                .average().getAsDouble();
    }

    public double averageEffectivenessOfTreatment() {
        return findAll().stream().map(review -> review.getEffectivenessOfTreatment())
                .flatMap(Optional::stream)
                .mapToDouble(value -> value)
                .average().getAsDouble();
    }

    public double averageRatingOverall() {
        return findAll().stream().map(review -> review.getRatingOverall())
                .flatMap(Optional::stream)
                .mapToDouble(value -> value)
                .average().getAsDouble();
    }

    public Map<LocalDateTime, List<String>> mapDateToComment() {
        return findAll().stream().filter(review -> review.getComment()
                .isPresent()).collect(Collectors
                .groupingBy(Review::getLocalDateTimeReview, Collectors
                        .flatMapping(review -> review.getComment()
                                .stream(), Collectors.toList())));
    }

    @Data
    public class Report {
        public double averageService;
        public double averageEquipment;
        public double averageQualificationSpecialist;
        public double averageEffectivenessOfTreatment;
        public double averageRatingOverall;
        public Map<LocalDateTime, List<String>> mapDateToComment;

        public Report initReport(Report report) {
            report.setAverageService(ReviewService.this.averageService());
            report.setAverageEquipment(ReviewService.this.averageEquipment());
            report.setAverageQualificationSpecialist(ReviewService.this.averageQualificationSpecialist());
            report.setAverageEffectivenessOfTreatment(ReviewService.this.averageEffectivenessOfTreatment());
            report.setAverageRatingOverall(ReviewService.this.averageRatingOverall());
            report.setMapDateToComment(ReviewService.this.mapDateToComment());
            return report;
        }
    }
}
