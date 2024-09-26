package org.gds.poc.orch.manager.library;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrchProcessRequest(
        @NotBlank(message = "L'identificato processo di orchestrazione è obbligatorio")
        String uuid,
        String stateMachineState,
        BusinessState businessState,
        String ctx,
        String result) {
}
