package hillel.doctorRest.doctor;

import lombok.AllArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor

public class IdGenerator {
    private final AtomicInteger id = new AtomicInteger();


    public Integer generateId() {
        return id.getAndIncrement() + 1;
    }

}


