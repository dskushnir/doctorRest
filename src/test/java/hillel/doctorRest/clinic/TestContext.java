package hillel.doctorRest.clinic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;



@Configuration

public class TestContext {
    @Primary
    @Bean
    public Clock testClock(){
        var instant = LocalDateTime.parse("2019-01-01T12:00:00").toInstant(ZoneOffset.UTC);
        return Clock.fixed(instant, ZoneId.systemDefault());
    }
}
