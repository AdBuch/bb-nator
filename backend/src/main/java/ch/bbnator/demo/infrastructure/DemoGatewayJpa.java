package ch.bbnator.demo.infrastructure;

import ch.bbnator.demo.application.DemoLearningService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

/** Spring Data repository adapter scoped to one fictional demo tenant. */
@Repository
@Profile("demo")
public class DemoGatewayJpa implements DemoLearningService.Repository {
    private static final String TENANT = "demo";
    private final EntityManager entityManager;
    private final DemoLearnerRepository learners;
    private final DemoTaskRepository tasks;

    public DemoGatewayJpa(EntityManager entityManager, DemoLearnerRepository learners, DemoTaskRepository tasks) {
        this.entityManager = entityManager;
        this.learners = learners;
        this.tasks = tasks;
    }
    private void setTenant() {
        entityManager.createNativeQuery("SELECT set_config('app.tenant_id', :tenant, true)")
            .setParameter("tenant", TENANT).getSingleResult();
    }
    private DemoLearningService.Task toTask(DemoTaskEntity t) {
        return new DemoLearningService.Task(t.id,t.learnerId,t.title,t.learningGoal,t.status,t.evidence);
    }
    @Override @Transactional(readOnly=true)
    public List<DemoLearningService.Learner> learners() {
        setTenant();
        return learners.findAll().stream()
            .map(l -> new DemoLearningService.Learner(l.id,l.displayName,l.semester)).toList();
    }
    @Override @Transactional(readOnly=true)
    public List<DemoLearningService.Task> tasks() {
        setTenant();
        return tasks.findAll().stream().map(this::toTask).toList();
    }
    @Override @Transactional
    public DemoLearningService.Task create(DemoLearningService.CreateTask input) {
        setTenant();
        if (!learners.existsById(input.learnerId()))
            throw new IllegalArgumentException("Unknown demo learner");
        DemoTaskEntity t = new DemoTaskEntity();
        t.id = UUID.randomUUID();
        t.tenantId = TENANT;
        t.learnerId = input.learnerId();
        t.title = input.title();
        t.learningGoal = input.learningGoal();
        t.status = "ASSIGNED";
        t.evidence = "MISSING";
        return toTask(tasks.save(t));
    }
    @Override @Transactional
    public DemoLearningService.Task change(UUID id, DemoLearningService.UpdateTask update) {
        setTenant();
        DemoTaskEntity task = tasks.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown demo task"));
        task.status = update.status();
        task.evidence = update.evidence();
        return toTask(tasks.save(task));
    }
}
