package hillel.doctorRest.clinic.doctor;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("select doctor from Doctor as doctor  where  doctor.name=:name and doctor.specializations In (:specializations)")
    Page<Doctor> findByParameter(@Param("name") String name,
                                 @Param("specializations") List<String> specializations, Pageable pageable);


    Page<Doctor> findByNameIgnoreCase(String name,Pageable pageable);

    Page<Doctor> findBySpecializationsIn(List<String> specializations,Pageable pageable);


}







