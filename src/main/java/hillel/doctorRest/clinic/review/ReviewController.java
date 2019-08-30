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
        } else isValidInputDto(reviewInputDto);
            return reviewService.createReview(reviewDtoConverter
                    .toModel(scheduleId, reviewInputDto, LocalDateTime.now(clock)));
    }

    public boolean isValidInputDto (ReviewInputDto reviewInputDto){
        if(reviewInputDto.getService().filter(x->x>5).isPresent()){
            throw new ServiceIncorrectRatingException();
        }else if(reviewInputDto.getEquipment().filter(x->x>5).isPresent()){
            throw new EquipmentRatingException();
        }else if (reviewInputDto.getQualificationSpecialist().filter(x->x>5).isPresent()){
            throw  new QualificationSpecialistRatingException();
        }else if (reviewInputDto.getEffectivenessOfTreatment().filter(x->x>5).isPresent()){
            throw new EffectivenessOfTreatmentRatingExeption();
        }else if (reviewInputDto.getRatingOverall().filter(x->x>5).isPresent()){
            throw new RatingOverallExceptional();
        }else {
            return false;
        }
    }

    @PatchMapping("/schedule/review/{id}")
    @Retryable(StaleObjectStateException.class)
    public void patchReview (@RequestBody ReviewInputDto reviewInputDto,
                      @PathVariable Integer id) {
        if (reviewService.findById(id).isEmpty()) {
            throw new ReviewNotFoundException();
        }else isValidInputDto(reviewInputDto);
            val reviewBase = reviewService.findById(id).get();
            reviewDtoConverter.update(reviewBase, reviewInputDto);
            reviewService.saveReview(reviewBase);
        }
    }








