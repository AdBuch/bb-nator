package ch.bbnator.demo.application;

import ch.bbnator.demo.infrastructure.*;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/** Synthetic-only pilot. In production this module is disabled, NOT a substitute for LDAP roles. */
@Service
@Profile("demo")
public class DemoLearningService {
    private static final String TENANT = "demo";
    private final EntityManager em;
    private final DemoLearnerRepository learners;
    private final DemoTaskRepository tasks;

    public DemoLearningService(EntityManager em, DemoLearnerRepository learners, DemoTaskRepository tasks) {
        this.em = em;
        this.learners = learners;
        this.tasks = tasks;
    }
    private void setTenant() {
        em.createNativeQuery("SELECT set_config('app.tenant_id', :tenant, true)")
          .setParameter("tenant", TENANT).getSingleResult();
    }

    public record Learner(UUID id, String name, short semester) {}
    public record Task(UUID id, UUID learnerId, String title, String learningGoal, String status, String evidence) {}
    public record CreateTask(UUID learnerId, String title, String learningGoal) {}
    public record UpdateTask(String status, String evidence) {}

    @Transactional(readOnly = true)
    public List<Learner> learners() {
        setTenant();
        return learners.findAll().stream().map(l -> new Learner(l.id,l.displayName,l.semester)).toList();
    }
    @Transactional(readOnly = true)
    public List<Task> tasks() {
        setTenant();
        return tasks.findAll().stream().map(this::toTask).toList();
    }
    private Task toTask(DemoTaskEntity t) {
        return new Task(t.id,t.learnerId,t.title,t.learningGoal,t.status,t.evidence);
    }
    @Transactional
    public Task create(CreateTask input) {
        setTenant();
        if (input == null || input.learnerId() == null || !learners.existsById(input.learnerId()))
            throw new IllegalArgumentException("A known demo learner is required");
        if (input.title() == null || input.title().isBlank() || input.title().length() > 160 ||
            input.learningGoal() == null || input.learningGoal().isBlank() || input.learningGoal().length() > 160)
            throw new IllegalArgumentException("Title and goal are required (1..160 characters)");
        DemoTaskEntity t = new DemoTaskEntity();
        t.id = UUID.randomUUID();
        t.tenantId = TENANT;
        t.learnerId = input.learnerId();
        t.title = input.title().trim();
        t.learningGoal = input.learningGoal().trim();
        t.status = "ASSIGNED";
        t.evidence = "MISSING";
        return toTask(tasks.save(t));
    }
    @Transactional
    public Task change(UUID id, UpdateTask update) {
        setTenant();
        DemoTaskEntity task = tasks.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown task"));
        if (update == null ||
            !List.of("ASSIGNED","IN_PROGRESS","IN_REVIEW","DONE").contains(update.status()) ||
            !List.of("MISSING","SUBMITTED","VERIFIED").contains(update.evidence()))
            throw new IllegalArgumentException("Unknown status or evidence");
        // Demo rule: a finished task does not imply verified competence.
        if ("VERIFIED".equals(update.evidence()) && !"DONE".equals(update.status()))
            throw new IllegalArgumentException("Verify evidence only when task is done");
        task.status=update.status();
        task.evidence=update.evidence();
        return toTask(tasks.save(task));
    }
}
