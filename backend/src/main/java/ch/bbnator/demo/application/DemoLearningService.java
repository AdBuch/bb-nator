package ch.bbnator.demo.application;

import java.util.List;
import java.util.UUID;

/** Plain Java demo use case. JPA, transactions and tenant context live in infrastructure. */
public final class DemoLearningService {
    public record Learner(UUID id, String name, short semester) {}
    public record Task(UUID id, UUID learnerId, String title, String learningGoal, String status, String evidence) {}
    public record CreateTask(UUID learnerId, String title, String learningGoal) {}
    public record UpdateTask(String status, String evidence) {}

    public interface Repository {
        List<Learner> learners();
        List<Task> tasks();
        Task create(CreateTask request);
        Task change(UUID id, UpdateTask request);
    }
    private final Repository repo;
    public DemoLearningService(Repository repo) { this.repo = repo; }

    public List<Learner> learners() { return repo.learners(); }
    public List<Task> tasks() { return repo.tasks(); }

    public Task create(CreateTask request) {
        if (request == null || request.learnerId() == null)
            throw new IllegalArgumentException("A known demo learner is required");
        if (request.title() == null || request.title().isBlank() || request.title().length() > 160
            || request.learningGoal() == null || request.learningGoal().isBlank() || request.learningGoal().length() > 160)
            throw new IllegalArgumentException("Title and learning goal must have 1..160 characters");
        return repo.create(new CreateTask(request.learnerId(),
                request.title().trim(), request.learningGoal().trim()));
    }
    public Task change(UUID id, UpdateTask request) {
        if (request == null || request.status() == null || request.evidence() == null
            || !List.of("ASSIGNED","IN_PROGRESS","IN_REVIEW","DONE").contains(request.status())
            || !List.of("MISSING","SUBMITTED","VERIFIED").contains(request.evidence()))
            throw new IllegalArgumentException("Invalid task state");
        if ("VERIFIED".equals(request.evidence()) && !"DONE".equals(request.status()))
            throw new IllegalArgumentException("Evidence can only be verified on completed tasks");
        return repo.change(id, request);
    }
}
