package hillel.doctorRest.clinic.review;

import hillel.doctorRest.clinic.review.dto.*;
import hillel.doctorRest.clinic.schedule.Schedule;
import hillel.doctorRest.clinic.schedule.ScheduleService;
import hillel.doctorRest.clinic.schedule.dto.ScheduleInputDto;
import hillel.doctorRest.clinic.schedule.dto.ScheduleOutputDto;
import lombok.AllArgsConstructor;
import lombok.val;
import org.hibernate.StaleObjectStateException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public Review findById(@PathVariable Integer id) {
        val mayBeReview = reviewService.findById(id);
        return mayBeReview.orElseThrow(ReviewNotFoundException::new);
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
        } else return reviewService.createReview(reviewDtoConverter
                .toModel(scheduleId, reviewInputDto, LocalDateTime.now(clock)));
    }

    @PatchMapping("/schedule/review/{id}")
    @Retryable(StaleObjectStateException.class)
    public void patch(@RequestBody ReviewInputDto reviewInputDto,
                      @PathVariable Integer id) {
        if (reviewService.findById(id).isEmpty()) {
            throw new ReviewNotFoundException();
        } else {
            val review = reviewService.findById(id).get();
            reviewDtoConverter.update(review, reviewInputDto);
            reviewService.saveReview(review);
        }
    }



}





