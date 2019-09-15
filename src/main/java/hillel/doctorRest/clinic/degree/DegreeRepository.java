package hillel.doctorRest.clinic.degree;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DegreeRepository extends JpaRepository<Degree, Integer> {
    Optional<Degree> findByDegreeNumber(Integer degreeNumber);
}

