package org.gds.poc.orch.manager;

import org.gds.poc.orch.manager.library.BusinessState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrchestrationProcessRepository extends JpaRepository<OrchestrationProcess, String> {
    @Query("select p.businessState from OrchestrationProcess p where p.uuid = ?1")
    List<BusinessState> getBusinessStateByUuid(String uuid);
    @Query("select p.result from OrchestrationProcess p where p.uuid = ?1")
    List<String> getRestultByUuid(String uuid);
}
