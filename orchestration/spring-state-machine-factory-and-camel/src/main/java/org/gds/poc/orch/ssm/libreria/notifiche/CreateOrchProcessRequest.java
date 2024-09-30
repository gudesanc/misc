package org.gds.poc.orch.ssm.libreria.notifiche;

import org.gds.poc.orch.ssm.libreria.ProcessType;

import java.time.LocalDateTime;

public record CreateOrchProcessRequest(

        String uuid,
        LocalDateTime creationTimestamp,
        String parentUuid,
        String tenantId,
        String ente,
        String user,
        String area,
        String procedura,
        String stateMachineID,
        String domainOrchEndpoint,
        ProcessType processType,
        String stateMachineInitialState,
        String ctx) {


}
