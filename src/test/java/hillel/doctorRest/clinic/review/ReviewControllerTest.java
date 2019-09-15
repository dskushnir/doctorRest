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
    public void shouldReportReview() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 140))).getId();
        String idPet1 = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet("Tom"))).getId()).toString();
        Integer idSchedule1 = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1))).getId();
        Integer idSchedule2 = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2))).getId();
        reviewRepository.save(new Review(idSchedule1, LocalDateTime.now(clock), 4, 4, 4, 4, 4, "good"));
        reviewRepository.save(new Review(idSchedule2, LocalDateTime.now(clock), 5, 5, 5, 5, 5, "very good"));
        mockMvc.perform(MockMvcRequestBuilders.get("/schedule/review"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("averageService", Matchers.is(4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("averageEquipment", Matchers.is(4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("averageQualificationSpecialist", Matchers.is(4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("averageEffectivenessOfTreatment", Matchers.is(4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("averageRatingOverall", Matchers.is(4.5)))
                .andExpect(MockMvcResultMatchers.jsonPath("mapDateToComment", Matchers.hasValue(Matchers.contains("good", "very good"))));

    }

    @Test
    public void shouldCreateReview() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 141))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldCreateReviewNotFoundVisit() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 142))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId() + 1;
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateReviewIncorrectDate() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 143))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2020, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldCreateReviewIncorrectRating() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 144))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/schedule/{scheduleId}/review", idSchedule)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-create-incorrect-rating.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldPatchReview() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 145))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        Integer idReview = (reviewRepository.save(new Review(idSchedule, LocalDateTime.now(clock), 5, 5, 5, 5, 5, "very good"))).getId();
        mockMvc.perform(MockMvcRequestBuilders.patch("/schedule/review/{id}", idReview)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-update.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void shouldPatchReviewIncorrectRating() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 146))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        Integer idReview = (reviewRepository.save(new Review(idSchedule, LocalDateTime.now(clock), 5, 5, 5, 5, 5, "very good"))).getId();
        mockMvc.perform(MockMvcRequestBuilders.patch("/schedule/review/{id}", idReview)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-update-incorrect-rating.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldPatchReviewNotFound() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 147))).getId();
        String idPet = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        Integer idSchedule = (scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet))).getId();
        Integer idReview = (reviewRepository.save(new Review(idSchedule, LocalDateTime.now(clock), 5, 5, 5, 5, 5, "very good"))).getId() + 1;
        mockMvc.perform(MockMvcRequestBuilders.patch("/schedule/review/{id}", idReview)
                .contentType("application/json")
                .content(fromResource("clinic/review/review-update.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    public String fromResource(String path) {
        try {
            File file = ResourceUtils.getFile("classpath:" + path);
            return Files.readString(file.toPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}