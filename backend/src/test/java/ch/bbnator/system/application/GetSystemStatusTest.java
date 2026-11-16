package ch.bbnator.system.application;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GetSystemStatusTest {
    private final GetSystemStatus useCase = new GetSystemStatus();

    @Test
    void returnsTechnicalSystemStatus() {
        var result = useCase.execute();
        assertEquals("bb-nator", result.service());
        assertEquals("READY", result.status());
        assertEquals("0.1.0", result.version());
    }
}
