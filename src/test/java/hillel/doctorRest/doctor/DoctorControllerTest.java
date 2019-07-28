package hillel.doctorRest.doctor;

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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
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
    public void shouldCreateDoctor() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/doctors")
                .contentType("application/json")
                .content(fromResource("doctor/create-doctor.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("location", Matchers.containsString("http://localhost/doctors/")))
                .andReturn().getResponse();
        Integer id = Integer.parseInt(response.getHeader("location").replace("http://localhost/doctors/", ""));
        Assertions.assertThat(doctorRepository.findById(id)).isPresent();
    }

    @Test
    public void shouldUpdateDoctorNoFoundId() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "therapist")).getId() + 1;
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldUpdateDoctor() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "therapist")).getId();
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id).get().getSpecialization()).isEqualTo("surgeon");
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "surgeon")).getId();
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldDeleteDoctorByIdNotFound() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "therapist")).getId() + 1;
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldFindAll() throws Exception {
        doctorRepository.create(new Doctor(null, "Jack", "therapist"));
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        doctorRepository.create(new Doctor(null, "Alex", "surgeon"));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(fromResource("doctor/all-doctors.json"), false))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Jack")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Adam")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].name", Matchers.is("Alex")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].specialization", Matchers.is("therapist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].specialization", Matchers.is("therapist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].specialization", Matchers.is("surgeon")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id", Matchers.notNullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].id", Matchers.notNullValue()));
    }

    @Test
    public void shouldReturnById() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "therapist")).getId();
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        String stringId = Integer.toString(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("id", stringId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Jack")));
    }

    @Test
    public void shouldReturnByIdNotFound() throws Exception {
        Integer id = doctorRepository.create(new Doctor(null, "Jack", "therapist")).getId() + 1;
        String stringId = Integer.toString(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("id", stringId))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldReturnByNameJack() throws Exception {
        doctorRepository.create(new Doctor(null, "Alex", "therapist"));
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        doctorRepository.create(new Doctor(null, "Jack", "surgeon"));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("name", "Alex"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnByFirstLatterNameAlex() throws Exception {
        doctorRepository.create(new Doctor(null, "Alex", "therapist"));
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        doctorRepository.create(new Doctor(null, "Jack", "surgeon"));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("nameLetter", "A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Alex")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name", Matchers.is("Adam")));
    }


    @Test
    public void shouldReturnBySpecializationAlex() throws Exception {
        doctorRepository.create(new Doctor(null, "Jack", "therapist"));
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        doctorRepository.create(new Doctor(null, "Alex", "surgeon"));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("specialization", "surgeon"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnBySpecializationNoFound() throws Exception {
        doctorRepository.create(new Doctor(null, "Jack", "therapist"));
        doctorRepository.create(new Doctor(null, "Adam", "therapist"));
        doctorRepository.create(new Doctor(null, "Alex", "therapist"));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("specialization", "surgeon"))
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