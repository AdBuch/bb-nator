package ch.bbnator.system.domain;

/** Framework-free system status value object. */
public record ServiceStatus(String service, String status, String version) {
}
