package hillel.doctorRest.clinic.pet;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class PetService {
    private final PetRepository petRepository;
    public Optional<Pet> findById(Integer id){
        return petRepository.findById(id);
    }
    public List<Pet>findAll(){
        return petRepository.findAll();

    }
    public Pet createPet(Pet pet){
        return petRepository.save(pet);
    }

}
