package ch.bbnator.demo.infrastructure;

import ch.bbnator.demo.application.DemoLearningService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("demo")
public class DemoModuleConfiguration {
    @Bean
    DemoLearningService demoLearningService(DemoLearningService.Repository repository) {
        return new DemoLearningService(repository);
    }
}
