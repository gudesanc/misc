package org.gds.poc.orch.manager.library;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateOrchProcessRequest(

        @NotBlank(message = "L'identificativo del processo di orchestrazione non può essere vuoto")
        String uuid,
        @NotNull(message = "La data di creazione processo è obbligatoria")
        LocalDateTime creationTimestamp,
        String parentUuid,
        @NotBlank(message = "L'identificativo del tenant è obbligatorio")
        String tenantId,
        @NotBlank(message = "L'identificativo dell'ente è obbligatorio")
        String ente,
        String user,
        @NotBlank(message = "L'area per la quale è partito il processo è obbligatoria")
        String area,
        @NotBlank(message = "La procedura per la quale è partito il processo è obbligatoria")
        String procedura,
        @NotBlank(message = "L'identificativo del tipo di orchestrazione è obbligatorio")
        String stateMachineID,
        @NotBlank(message = "L'endpoint del controlle che gestisce l'orchestrazione è obbligatorio")
        String domainOrchEndpoint,
        @NotNull(message = "Il tipo di processo è obbligatorio")
        ProcessType processType,
        @NotBlank(message = "Lo stato iniziale del processo è obbligatoria")
        String stateMachineInitialState,
        String ctx) {


}
