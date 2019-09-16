package hillel.doctorRest.clinic.doctor;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import hillel.doctorRest.TestRunner;
import hillel.doctorRest.clinic.degree.DegreeRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Rule;
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

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8089);
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    DegreeRepository degreeRepository;

    @After
    public void cleanup() {
        doctorRepository.deleteAll();
    }

    @Test
    public void shouldCreateDoctorNotFoundSpecialization() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22201").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto-not-found-specialization.json"))));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22201)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctors-not-found-specialization.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldCreateDoctorNullName() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22202").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto-doctor-null-name.json"))));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22202)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor-null-name.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    public void shouldCreateDoctorEmptyName() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22203").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto-doctor-empty-name.json"))));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22203)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor-name-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldCreateDoctorNullSpecialization() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22204").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto-null-specialization.json"))));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22204)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor-specialization-null.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldCreateDoctorSpecializationEmpty() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22205").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto-empty-specialization.json"))));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22205)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor-specialization-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
    @Test
    public void shouldCreateDoctorNotFoundDegree() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/2").willReturn(WireMock.notFound()));
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 2)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse();
    }
    
    @Test
    public void shouldCreateDoctor() throws Exception {
        WireMock.stubFor(WireMock.get("/degrees/22222").willReturn(WireMock.okJson(fromResource("clinic/degree/degree-inputDto.json"))));
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{degreeNumber}", 22222)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/create-doctor.json")))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("location", Matchers.containsString("http://my-doctor.com/doctors/")))
                .andReturn().getResponse();
        Integer id = Integer.parseInt(response.getHeader("location").replace("http://my-doctor.com/doctors/", ""));
        Assertions.assertThat(doctorRepository.findById(id)).isPresent();
        var doctor = doctorRepository.findById(id);
        Assertions.assertThat(degreeRepository.findByDegreeNumber(doctor.get().getDegreeNumber())).isPresent();
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathEqualTo("/degrees/22222")));
    }

    @Test
    public void shouldUpdateDoctorNoFoundSpecialization() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33301)).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-not-found-specialization.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctorNullSpecialization() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33302)).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-specialization-null.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctorSpecializationEmpty() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33303)).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-specialization-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctorNameEmpty() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33304)).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-name-empty.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctorNullName() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33305)).getId();
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctor-name-null.json")))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void shouldUpdateDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33333)).getId();
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 33334));
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", id)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id).get().getSpecializations()).contains("surgeon");
    }

    @Test
    public void shouldUpdateDoctorInvalidId() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33306)).getId();
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 33307));
        mockMvc.perform(MockMvcRequestBuilders.put("/doctors/{id}", 0)
                .contentType("application/json")
                .content(fromResource("clinic/doctor/update-doctors.json")))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void shouldDeleteDoctor() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("surgeon"), 33335)).getId();
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 33336));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldDeleteDoctorByIdNotFound() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33308)).getId() + 1;
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 33309));
        mockMvc.perform(MockMvcRequestBuilders.delete("/doctors/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Assertions.assertThat(doctorRepository.findById(id)).isEmpty();
    }

    @Test
    public void shouldFindAll() throws Exception {
        doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 33310));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 33311));
        doctorRepository.save(new Doctor("Alex", Arrays.asList("surgeon"), 33312));
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].specializations").value("surgeon"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].degreeNumber").value(33310))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].degreeNumber").value(33311))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[2].degreeNumber").value(33312));
    }

    @Test
    public void shouldReturnById() throws Exception {
        Integer id = doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 44444)).getId();
        String stringId = Integer.toString(id);
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("id", stringId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specializations").value("therapist"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].degreeNumber").value(44444));
    }

    @Test
    public void shouldReturnByNameAlex() throws Exception {
        doctorRepository.save(new Doctor("Alex", Arrays.asList("therapist"), 55551));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 55552));
        doctorRepository.save(new Doctor("Jack", Arrays.asList("surgeon"), 55553));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors").param("name", "Alex"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnBySpecializationAlex() throws Exception {
        doctorRepository.save(new Doctor("Jack", Arrays.asList("cardiologist"), 55554));
        doctorRepository.save(new Doctor("Alex", Arrays.asList("surgeon", "therapist"), 55555));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                .param("specializations", "therapist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name", Matchers.is("Alex")));
    }

    @Test
    public void shouldReturnBySpecializations() throws Exception {
        doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 55556));
        doctorRepository.save(new Doctor("Alex", Arrays.asList("cardiologist"), 55557));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 55558));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("surgeon"), 55559));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors")
                .param("specializations", "surgeon", "cardiologist"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].specializations", Matchers.contains("cardiologist")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[1].specializations", Matchers.contains("surgeon")));
    }

    @Test
    public void shouldReturnBySpecializationNotFound() throws Exception {
        doctorRepository.save(new Doctor("Jack", Arrays.asList("therapist"), 55501));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("cardiologist"), 55502));
        doctorRepository.save(new Doctor("Adam", Arrays.asList("therapist"), 55503));
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









