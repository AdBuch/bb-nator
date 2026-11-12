package ch.bbnator.system.infrastructure;

import ch.bbnator.system.application.GetSystemStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemModuleConfiguration {
    @Bean
    GetSystemStatus getSystemStatus() {
        return new GetSystemStatus();
    }
}
