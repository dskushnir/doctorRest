package hillel.doctorRest.clinic.schedule;

import hillel.doctorRest.TestRunner;
import hillel.doctorRest.clinic.doctor.Doctor;
import hillel.doctorRest.clinic.doctor.DoctorRepository;
import hillel.doctorRest.clinic.pet.Pet;
import hillel.doctorRest.clinic.pet.PetRepository;
import hillel.doctorRest.clinic.pet.PetService;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

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
    public void shouldCreateSchedule() throws Exception {
        Integer id = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        petRepository.save(new Pet(null, "Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void shouldHourToPetId() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex",Arrays.asList("therapist")))).getId();
        String idPet1 = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet(null, "Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1));
        scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}/schedule/{visitDate}", idDoctor, LocalDate.of(2010, 1, 1)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("hourToPetId", Matchers.hasEntry("8", idPet1)))
                .andExpect(MockMvcResultMatchers.jsonPath("hourToPetId", Matchers.hasEntry("9", idPet2)));
    }

    @Test
    public void shouldHourToPetIdNotFoundDoctor() throws Exception {
        Integer idDoctor = (doctorRepository.save(new Doctor(null, "Alex",Arrays.asList("therapist")))).getId() + 1;
        String idPet1 = ((petRepository.save(new Pet(null, "Donald"))).getId()).toString();
        String idPet2 = ((petRepository.save(new Pet(null, "Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "8", idPet1));
        scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), idDoctor, "9", idPet2));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}/schedule/{visitDate}", idDoctor, LocalDate.of(2010, 1, 1)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotFoundDoctor() throws Exception {
        Integer id = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId() + 1;
        petRepository.save(new Pet(null, "Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotFoundPet() throws Exception {
        Integer id = (doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")))).getId();
        petRepository.save(new Pet(null, "Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-createNotFoundPet.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldCreateScheduleNotHour() throws Exception {
        Integer id = (doctorRepository.save(new Doctor(null, "Alex",Arrays.asList("therapist")))).getId();
        String idPet1 = ((petRepository.save(new Pet(null, "Tom")).getId())).toString();
        scheduleRepository.save(new Schedule(null, LocalDate.of(2010, 1, 1), id, "8", idPet1));
        petRepository.save(new Pet(null, "Tom"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}", id, LocalDate.of(2010, 1, 1), "8")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/schedule-create.json")))
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