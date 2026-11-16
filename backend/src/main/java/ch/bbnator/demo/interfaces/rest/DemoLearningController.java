package ch.bbnator.demo.interfaces.rest;

import ch.bbnator.demo.application.DemoLearningService;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

/** Only available in explicit demo profile and local-only Docker binding. */
@Profile("demo")
@RestController
@RequestMapping("/api/v1/demo")
public class DemoLearningController {
    private final DemoLearningService service;
    public DemoLearningController(DemoLearningService service) { this.service = service; }

    @GetMapping("/learners")
    List<DemoLearningService.Learner> learners() { return service.learners(); }

    @GetMapping("/tasks")
    List<DemoLearningService.Task> tasks() { return service.tasks(); }

    @PostMapping("/tasks")
    ResponseEntity<DemoLearningService.Task> create(@RequestBody DemoLearningService.CreateTask input) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(input));
    }

    @PatchMapping("/tasks/{id}")
    DemoLearningService.Task update(@PathVariable UUID id, @RequestBody DemoLearningService.UpdateTask input) {
        return service.change(id,input);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<String> badRequest(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
