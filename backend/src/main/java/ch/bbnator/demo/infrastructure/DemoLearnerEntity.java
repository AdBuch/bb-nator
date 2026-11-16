package ch.bbnator.demo.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "demo_learner")
public class DemoLearnerEntity {
    @Id public UUID id;
    @Column(name = "tenant_id", nullable = false) public String tenantId;
    @Column(name = "display_name", nullable = false) public String displayName;
    @Column(nullable = false) public short semester;
    protected DemoLearnerEntity() {}
}
