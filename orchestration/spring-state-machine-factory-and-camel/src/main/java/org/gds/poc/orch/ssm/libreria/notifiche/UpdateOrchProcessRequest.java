package org.gds.poc.orch.ssm.libreria.notifiche;



import org.gds.poc.orch.ssm.libreria.BusinessState;

import java.time.LocalDateTime;

public record UpdateOrchProcessRequest(
        String uuid,
        LocalDateTime eventTimestamp,
        String stateMachineState,
        BusinessState businessState,
        String ctx,
        String result) {
}
