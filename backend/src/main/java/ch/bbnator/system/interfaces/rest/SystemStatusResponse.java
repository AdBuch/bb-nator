package ch.bbnator.system.interfaces.rest;

import ch.bbnator.system.domain.ServiceStatus;

/** REST representation of the technical status; keeps the domain record out of the HTTP contract. */
public record SystemStatusResponse(String service, String status, String version) {
    static SystemStatusResponse from(ServiceStatus status) {
        return new SystemStatusResponse(status.service(), status.status(), status.version());
    }
}
