package hillel.doctorRest.clinic.review;

import hillel.doctorRest.clinic.review.dto.*;
import hillel.doctorRest.clinic.schedule.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor

public class ReviewController {
    private final ReviewService reviewService;
    private final ScheduleService scheduleService;
    private final ReviewDtoConverter reviewDtoConverter;
    private final ReviewModelConverter reviewModelConverter;
    private final ReportDtoConverter reportDtoConverter;
    private Clock clock;

    @GetMapping("/schedule/review/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewOutputDto findById(@PathVariable Integer id) {
        val mayBeReview = reviewService.findById(id);
        return reviewModelConverter.reviewToDto(mayBeReview.orElseThrow(ReviewNotFoundException::new));
    }

    @GetMapping("/schedule/review")
    @ResponseStatus(HttpStatus.OK)
    public List<Object> reportReview() {
        val reports = reviewService.reportReview();
        return reportDtoConverter.toDtoReportReview(reports);
    }

    @PostMapping("/schedule/{scheduleId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(@PathVariable Integer scheduleId,
                               @RequestBody ReviewInputDto reviewInputDto) {
        if (scheduleService.findById(scheduleId).isEmpty()) {
            throw new VisitNotFoundException();
        } else if (scheduleService.dateTimeSchedule(scheduleId).isAfter(LocalDateTime.now(clock))) {
            throw new DateTimeReviewIncorrectException();
        }  else if(reviewInputDto.getService().filter(x->x>5).isPresent()){
            throw new ServiceIncorrectRatingException();
        }else if(reviewInputDto.getEquipment().filter(x->x>5).isPresent()){
            throw new EquipmentRatingException();
        }else if (reviewInputDto.getQualificationSpecialist().filter(x->x>5).isPresent()){
            throw  new QualificationSpecialistRatingException();
        }else if (reviewInputDto.getEffectivenessOfTreatment().filter(x->x>5).isPresent()){
            throw new EffectivenessOfTreatmentRatingExeption();
        }else if (reviewInputDto.getRatingOverall().filter(x->x>5).isPresent()) {
            throw new RatingOverallExceptional();
        }else {
            return reviewService.createReview(reviewDtoConverter
                    .toModel(scheduleId, reviewInputDto, LocalDateTime.now(clock)));
        }
    }

    @PatchMapping("/schedule/review/{id}")
    @Retryable(StaleObjectStateException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchReview (@RequestBody ReviewInputForUpdateDto reviewInputForUpdateDto,
                      @PathVariable Integer id) {
        if (reviewService.findById(id).isEmpty()) {
            throw new ReviewNotFoundException();
        } else if (reviewInputForUpdateDto.getService() > 5) {
            throw new ServiceIncorrectRatingException();
        } else if (reviewInputForUpdateDto.getEquipment() > 5) {
            throw new EquipmentRatingException();
        } else if (reviewInputForUpdateDto.getQualificationSpecialist() > 5) {
            throw new QualificationSpecialistRatingException();
        } else if (reviewInputForUpdateDto.getEffectivenessOfTreatment() > 5) {
            throw new EffectivenessOfTreatmentRatingExeption();
        } else if (reviewInputForUpdateDto.getRatingOverall() > 5) {
            throw new RatingOverallExceptional();
        } else {
            val reviewBase = reviewService.findById(id).get();
            reviewDtoConverter.update(reviewBase, reviewInputForUpdateDto);
            reviewService.saveReview(reviewBase);
        }
    }
}








