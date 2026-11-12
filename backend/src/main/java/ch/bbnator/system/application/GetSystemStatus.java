package ch.bbnator.system.application;

import ch.bbnator.system.domain.ServiceStatus;

/** Plain-Java use case; no Spring, persistence or HTTP dependency. */
public final class GetSystemStatus {
    public ServiceStatus execute() {
        return new ServiceStatus("bb-nator", "READY", "0.1.0");
    }
}
