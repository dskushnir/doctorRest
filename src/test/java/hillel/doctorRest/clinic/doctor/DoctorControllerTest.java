package hillel.doctorRest.clinic.doctor;

import hillel.doctorRest.TestRunner;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@TestRunner
public class DoctorControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepository;

    @After
    public void cleanup() {
        doctorRepository.deleteAll();
    }

    @Test
    public void shouldCreateDoctorNotFoundSpecialization() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctors-not-found-specialization.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse();
    }

    @Test
    public void shouldCreateDoctor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("location", Matchers.containsString("http://my-doctor.com/doctors/")))
                .andReturn().getResponse();
        Integer id = Integer.parseInt(response.getHeader("location").replace("http://my-doctor.com/doctors/", ""));
        Assertions.assertThat(doctorRepository.findById(id)).isPresent();
    }

    @Test
    public void shouldUpdateDoctorNoFoundSpecialization() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist"))).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-not-found-specialization.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist"))).getId();
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id).get().getSpecializations()).contains("surgeon");
    }

    @Test
    public void shouldUpdateDoctorInvalidId() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist"))).getId();
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", 0)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("surgeon"))).getId();
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldDeleteDoctorByIdNotFound() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist"))).getId() + 1;
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldFindAll() throws Exception {
        doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("surgeon")));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fromResource("clinic/doctor/all-doctors.json"), false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$['pageable']['paged']").value("true"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("Adam"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name").value("Alex"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specializations").value("therapist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].specializations").value("therapist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].specializations").value("surgeon"));
    }

    @Test
    public void shouldReturnById() throws Exception {
        Integer id = doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist"))).getId();
        String stringId = Integer.toString(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("id", stringId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specializations").value("therapist"));
    }

    @Test
    public void shouldReturnByNameJack() throws Exception {
        doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("surgeon")));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("name", "Alex"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnBySpecializationAlex() throws Exception {
        doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("cardiologist")));
        doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("surgeon", "therapist")));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                .param("specializations", "therapist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnBySpecializations() throws Exception {
        doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Alex", Arrays.asList("cardiologist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("surgeon")));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                .param("specializations", "surgeon", "cardiologist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specializations", Matchers.contains("cardiologist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].specializations", Matchers.contains("surgeon")));
    }

    @Test
    public void shouldReturnBySpecializationNotFound() throws Exception {
        doctorRepository.save(new Doctor(null, "Jack", Arrays.asList("therapist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("cardiologist")));
        doctorRepository.save(new Doctor(null, "Adam", Arrays.asList("therapist")));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("specializations", "surgeon"))
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









