package hillel.doctorRest.clinic.review;

import hillel.doctorRest.TestRunner;
import hillel.doctorRest.clinic.doctor.Doctor;
import hillel.doctorRest.clinic.doctor.DoctorRepository;
import hillel.doctorRest.clinic.pet.Pet;
import hillel.doctorRest.clinic.pet.PetRepository;
import hillel.doctorRest.clinic.schedule.Schedule;
import hillel.doctorRest.clinic.schedule.ScheduleRepository;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestRunner

public class ReviewControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    Clock clock;



    @After
    public void cleanup() {
        doctorRepository.deleteAll();
        petRepository.deleteAll();
        scheduleRepository.deleteAll();
        reviewRepository.deleteAll();
    }

    @Test
    public void shouldReportReview() throws Exception{
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        String idPet1 = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet(null, "Tom"))).getId()).toString();
        Integer idSchedule1=(scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1))).getId();
        Integer idSchedule2=(scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2))).getId();
        reviewRepository.save(new Review(null,null,idSchedule1,LocalDateTime.now(clock),4,4,4,4,4,"good"));
        reviewRepository.save(new Review(null,null,idSchedule2,LocalDateTime.now(clock),5,5,5,5,5,"very good"));
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/review"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.hasEntry("averageRatingOverall",4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.hasEntry("averageService",4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.hasEntry("averageEffectivenessOfTreatment",4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.hasEntry("averageEquipment",4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0]",Matchers.hasEntry("averageQualificationSpecialist",4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1]",Matchers.hasValue(Matchers.contains("good", "very good"))));

    }

    @Test
    public void shouldCreateReview()throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        String idPet = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        Integer idSchedule=(scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }
    @Test
    public void shouldCreateReviewNotFoundVisit()throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        String idPet = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId()+1;
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
    @Test
    public void shouldCreateReviewIncorrectDate()throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        String idPet = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        Integer idSchedule=(scheduleRepository.save(new Schedule(null, LocalDate.of(2020, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void shouldCreateReviewIncorrectRating()throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        String idPet = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        Integer idSchedule=(scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create-incorrect-rating.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }


 /*   @Test
    public void patchReview() {
    }*/




    public String fromResource(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            return Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}