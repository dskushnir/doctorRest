package hillel.doctorRest.clinic.review;

import hillel.doctorRest.clinic.review.dto.*;
import hillel.doctorRest.clinic.schedule.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.hibernate.StaleObjectStateException;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@AllArgsConstructor

public class ReviewController {
    private final ReviewService reviewService;
    private final ScheduleService scheduleService;
    private final ReviewDtoConverter reviewDtoConverter;
    private final ReviewModelConverter reviewModelConverter;
    private Clock clock;

    @GetMapping("/schedule/review/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ReviewOutputDto findById(@PathVariable Integer id) {
        val mayBeReview = reviewService.findById(id);
        return reviewModelConverter.reviewToDto(mayBeReview
                .orElseThrow(ReviewNotFoundException::new));
    }

    @GetMapping("/schedule/review")
    @ResponseStatus(HttpStatus.OK)
    public ReportOutputDto reportView() {
        ReviewService.Report report = reviewService.new Report();
        return reviewModelConverter
                .reportToDto(report.initReport(report));
    }

    @PostMapping("/schedule/{scheduleId}/review")
    @ResponseStatus(HttpStatus.CREATED)
    public Review createReview(@PathVariable Integer scheduleId,
                               @Valid @RequestBody ReviewInputDto reviewInputDto) {
        if (scheduleService.findById(scheduleId).isEmpty()) {
            throw new VisitNotFoundException();
        } else if (scheduleService.dateTimeSchedule(scheduleId)
                .isAfter(LocalDateTime.now(clock))) {
            throw new DateTimeReviewIncorrectException();
        } else {
            return reviewService.createReview(reviewDtoConverter
                    .toModel(scheduleId, reviewInputDto, LocalDateTime.now(clock)));
        }
    }

    @PatchMapping("/schedule/review/{id}")
    @Retryable(StaleObjectStateException.class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void patchReview(@Valid @RequestBody ReviewInputForUpdateDto reviewInputForUpdateDto,
                            @PathVariable Integer id) {
        if (reviewService.findById(id).isEmpty()) {
            throw new ReviewNotFoundException();
        }
        val reviewBase = reviewService.findById(id).get();
        reviewDtoConverter.update(reviewBase, reviewInputForUpdateDto);
        reviewService.saveReview(reviewBase);
    }
}










