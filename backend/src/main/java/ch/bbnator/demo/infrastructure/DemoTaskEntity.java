package ch.bbnator.demo.infrastructure;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.util.UUID;

@Entity
@Table(name = "demo_task")
public class DemoTaskEntity {
    @Id public UUID id;
    @Column(name = "tenant_id", nullable = false) public String tenantId;
    @Column(name = "learner_id", nullable = false) public UUID learnerId;
    @Column(nullable = false) public String title;
    @Column(name = "learning_goal", nullable = false) public String learningGoal;
    @Column(nullable = false) public String status;
    @Column(nullable = false) public String evidence;
    protected DemoTaskEntity() {}
}
