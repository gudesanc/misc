package org.gds.poc.orch.manager.library;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record UpdateOrchProcessRequest(
        @NotBlank(message = "L'identificato processo di orchestrazione è obbligatorio")
        String uuid,
        @NotNull(message = "La data dell'evento che ha portato all'aggiornamento è obbligatoria")
        LocalDateTime eventTimestamp,
        String stateMachineState,
        BusinessState businessState,
        String ctx,
        String result) {
}
