package ch.bbnator.system.interfaces.rest;

import ch.bbnator.system.application.GetSystemStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Public technical endpoint only. Never expose personal learner information here. */
@RestController
@RequestMapping("/api/v1/system")
public class SystemController {
    private final GetSystemStatus useCase;

    public SystemController(GetSystemStatus useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/status")
    public SystemStatusResponse status() {
        return SystemStatusResponse.from(useCase.execute());
    }
}
