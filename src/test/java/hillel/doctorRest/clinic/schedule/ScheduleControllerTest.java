package hillel.doctorRest.clinic.schedule;

import hillel.doctorRest.TestRunner;
import hillel.doctorRest.clinic.doctor.Doctor;
import hillel.doctorRest.clinic.doctor.DoctorRepository;
import hillel.doctorRest.clinic.pet.Pet;
import hillel.doctorRest.clinic.pet.PetRepository;
import org.assertj.core.api.Assertions;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@TestRunner
public class ScheduleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    @After
    public void cleanup() {
        doctorRepository.deleteAll();
        petRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    public void shouldHourToPetId() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),111))).getId();
        String idPet1 = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1));
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}/schedule/{visitDate}", idDoctor, LocalDate.of(2010, 1, 1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("hourToPetId", Matchers.hasEntry("8", idPet1)))
                .andExpect(MockMvcResultMatchers.jsonPath("hourToPetId", Matchers.hasEntry("9", idPet2)));
    }

    @Test
    public void shouldCreateSchedule() throws Exception {
        Integer id = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),123))).getId();
        petRepository.save(new Pet("Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldHourToPetIdNotFoundDoctor() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),124))).getId() + 1;
        String idPet1 = ((petRepository.save(new Pet("Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1));
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}/schedule/{visitDate}", idDoctor, LocalDate.of(2010, 1, 1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotFoundDoctor() throws Exception {
        Integer id = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),125))).getId() + 1;
        petRepository.save(new Pet("Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotFoundPet() throws Exception {
        Integer id = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),126))).getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-createNotFoundPet.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotHour() throws Exception {
        Integer id = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),127))).getId();
        String idPet1 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), id, "8", idPet1));
        petRepository.save(new Pet("Tom"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "8")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldSwapListDoctors() throws Exception {
        Integer idDoc1 = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),128))).getId();
        String idPet1 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc1, "15", idPet1));
        Integer idDoc2 = (doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"),129))).getId();
        String idPet2 = ((petRepository.save(new Pet("Donald")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc2, "14", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/swap-doctors/{visitDate}/{doctor1Id}/{doctor2Id}", LocalDate.of(2010, 1, 1), idDoc1, idDoc2))
                .andExpect(MockMvcResultMatchers.status().isOk());
        List<Schedule> scheduleDoc1 = scheduleRepository.findByDoctorIdAndVisitDate(idDoc1, LocalDate.of(2010, 1, 1));
        List<Schedule> scheduleDoc2 = scheduleRepository.findByDoctorIdAndVisitDate(idDoc2, LocalDate.of(2010, 1, 1));
        Assertions.assertThat(scheduleDoc1).isEmpty();
        Assertions.assertThat(scheduleDoc2).hasSize(2);
        Assertions.assertThat((scheduleDoc2.get(0)).getHour()).contains("14");
        Assertions.assertThat((scheduleDoc2.get(1)).getHour()).contains("15");
        Assertions.assertThat((scheduleDoc2.get(0)).getPetId()).contains(idPet2);
        Assertions.assertThat((scheduleDoc2.get(1)).getPetId()).contains(idPet1);
    }

    @Test
    public void shouldSwapDoctorsNotFound() throws Exception {
        Integer idDoc1 = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),130))).getId();
        String idPet1 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc1, "15", idPet1));
        Integer idDoc2 = (doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"),131))).getId();
        String idPet2 = ((petRepository.save(new Pet("Donald")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc2, "14", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/swap-doctors/{visitDate}/{doctor1Id}/{doctor2Id}", LocalDate.of(2010, 1, 1), idDoc1, idDoc2 + 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldSwapDoctorNotFoundSchedule() throws Exception {
        Integer idDoc1 = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),132))).getId();
        String idPet1 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        Integer idDoc2 = (doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"),133))).getId();
        String idPet2 = ((petRepository.save(new Pet("Donald")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc2, "14", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/swap-doctors/{visitDate}/{doctor1Id}/{doctor2Id}", LocalDate.of(2010, 1, 1), idDoc1, idDoc2))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldSwapDoctorNotFoundAvailableHour() throws Exception {
        Integer idDoc1 = (doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"),134))).getId();
        String idPet1 = ((petRepository.save(new Pet("Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc1, "12", idPet1));
        Integer idDoc2 = (doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"),135))).getId();
        String idPet2 = ((petRepository.save(new Pet("Donald")).getId())).toString();
        scheduleRepository.save(new Schedule(LocalDate.of(2010, 1, 1), idDoc2, "12", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/swap-doctors/{visitDate}/{doctor1Id}/{doctor2Id}", LocalDate.of(2010, 1, 1), idDoc1, idDoc2))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
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