package org.gds.poc.orch.manager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrchestrationProcessHistoryRepository extends JpaRepository<OrchestrationProcessHistory, String> {
}
