package hillel.doctorRest.clinic.review;

import hillel.doctorRest.clinic.schedule.Schedule;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import lombok.val;

@Service
@AllArgsConstructor

public class ReviewService {

private final ReviewRepository reviewRepository;
    public Review createReview(Review review) {
        return reviewRepository.save(review);
    }
    public Review saveReview (Review review){
        return reviewRepository.save(review);
    }
    public Optional<Review>findById(Integer id){
        return reviewRepository.findById(id);
    }

    public List<Review> findAll(){
        return reviewRepository.findAll();
    }
    public OptionalDouble averageService (){
        return findAll().stream().map(review -> review.getService()).flatMap(Optional::stream).mapToDouble(value -> value).average();

    }
    public OptionalDouble averageEquipment() {
      return findAll().stream().map(review -> review.getEquipment()).flatMap(Optional::stream).mapToDouble(value -> value).average();
    }

    public OptionalDouble averageQualificationSpecialist(){
        return findAll().stream().map(review -> review.getQualificationSpecialist()).flatMap(Optional::stream).mapToDouble(value -> value).average();
    }
    public OptionalDouble averageEffectivenessOfTreatment(){
        return findAll().stream().map(review -> review.getEffectivenessOfTreatment()).flatMap(Optional::stream).mapToDouble(value -> value).average();
    }
    public OptionalDouble averageRatingOverall(){
        return findAll().stream().map(review -> review.getRatingOverall()).flatMap(Optional::stream).mapToDouble(value -> value).average();
    }
    public Map<String,OptionalDouble>mapCriterionToAverage(){
        val map =new HashMap<String,OptionalDouble>();
        map.put("averageService",averageService());
        map.put("averageEquipment",averageEquipment());
        map.put("averageQualificationSpecialist",averageQualificationSpecialist());
        map.put("averageEffectivenessOfTreatment",averageEffectivenessOfTreatment());
        map.put("averageRatingOverall",averageRatingOverall());
        return map;
    }
    public Map<LocalDateTime,Optional<String>>mapDateToComment(){
        return   findAll().stream().filter(review -> review.getComment().isPresent())
                .collect(Collectors.toMap(Review::getLocalDateTimeReview, Review::getComment));
    }
    public List<Object>reportReview (){
        val list=new ArrayList<Object>();
        list.add(mapCriterionToAverage());
        list.add(mapDateToComment());
        return list;
    }






}
