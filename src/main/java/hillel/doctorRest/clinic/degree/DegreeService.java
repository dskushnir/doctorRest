package hillel.doctorRest.clinic.degree;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class DegreeService {
    private final DegreeRepository degreeRepository;

    public Optional<Degree> findByDegreeNumber(Integer degreeNumber) {
        return degreeRepository.findByDegreeNumber(degreeNumber);
    }

    public Degree createDegree(Degree degree) {
        return degreeRepository.save(degree);
    }
}
