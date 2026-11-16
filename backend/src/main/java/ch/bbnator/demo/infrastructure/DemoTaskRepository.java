package ch.bbnator.demo.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DemoTaskRepository extends JpaRepository<DemoTaskEntity, UUID> {}
