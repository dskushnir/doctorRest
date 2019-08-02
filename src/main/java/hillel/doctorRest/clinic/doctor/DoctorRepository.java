package hillel.doctorRest.clinic.doctor;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {
    @Query("select doctor from Doctor as doctor where doctor.name=:name and doctor.specialization In (:specializations)")
    List<Doctor> findByParameter(@Param("name") String name,
                                 @Param("specializations") List<String> specialization);

    List<Doctor> findByNameIgnoreCase(String name);

    List<Doctor> findBySpecializationIn(List<String> specializations);








   /*@Query("select doctor from Doctor as doctor where doctor.name=:name and doctor.specialization=:specialization")
    List<Doctor> findByParameter( @Param("name") String name,
                                  @Param("specialization")String specialization);*/


  //  List<Doctor> findBySpecialization(String specialization);




   // List<Doctor> findByNameStartWith(String nameStartWith);



}







