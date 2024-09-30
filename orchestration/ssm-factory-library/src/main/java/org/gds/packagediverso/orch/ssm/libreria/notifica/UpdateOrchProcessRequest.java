package org.gds.packagediverso.orch.ssm.libreria.notifica;



import org.gds.packagediverso.orch.ssm.libreria.BusinessState;

import java.time.LocalDateTime;

public record UpdateOrchProcessRequest(
        String uuid,
        LocalDateTime eventTimestamp,
        String stateMachineState,
        BusinessState businessState,
        String ctx,
        String result) {
}
