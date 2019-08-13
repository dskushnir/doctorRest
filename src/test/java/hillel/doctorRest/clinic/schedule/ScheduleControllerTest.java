package hillel.doctorRest.clinic.schedule;

import hillel.doctorRest.clinic.doctor.Doctor;
import hillel.doctorRest.clinic.doctor.DoctorRepository;
import hillel.doctorRest.clinic.pet.Pet;
import hillel.doctorRest.clinic.pet.PetRepository;
import hillel.doctorRest.clinic.pet.PetService;
import org.assertj.core.api.Assertions;
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
import java.util.Arrays;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc


public class ScheduleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PetRepository petRepository;
    @Autowired
    ScheduleRepository scheduleRepository;
    @Autowired
    PetService petService;
    @After
    public void cleanup(){
        doctorRepository.deleteAll();
        petRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    public void findAll() {
    }

    @Test
    public void shouldToPetId() {
       Integer idDoctor=( doctorRepository.save(new Doctor(null,"Alex",Arrays.asList("therapist")))).getId();
       String idPet1=(( petRepository.save(new Pet(null,"Donald"))).getId()).toString();
       String idPet2=((petRepository.save(new Pet(null,"Tom")).getId())).toString();
       scheduleRepository.save(new Schedule(null,"2010-01-01",idDoctor,"8",idPet1));
       scheduleRepository.save(new Schedule(null,"2010-01-01",idDoctor,"9",idPet2));
        mockMvc.perform(MockMvcRequestBuilders.get("/doctors/{doctorId}/schedule/{visitDate}")
                .param("doctorId", idDoctor.toString())
                .param("visitDate", "2010-01-01")
                .andExpect(MockMvcResultMatchers.status().isOk()));

    }

    @Test
    public void shouldCreateSchedule()throws Exception {
       Integer id=(doctorRepository.save(new Doctor(null,"Alex",Arrays.asList("therapist")))).getId();
       String stringId=id.toString();
        petRepository.save(new Pet(null,"Donald"));
        mockMvc.perform(MockMvcRequestBuilders.post("/doctors/{doctorId}/schedule/{visitDate}/{hour}")
                .param("doctorId",stringId)
                .param("visitDate","2010-01-01")
                .param("hour", "10")
                .contentType("application/json")
                .content(fromResource("clinic/schedule/create-schedule.json")))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

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