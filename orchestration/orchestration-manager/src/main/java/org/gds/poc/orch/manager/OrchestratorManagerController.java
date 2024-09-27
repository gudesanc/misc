package org.gds.poc.orch.manager;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.websocket.server.PathParam;
import org.gds.poc.orch.manager.library.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/orchestrazioni")
public class OrchestratorManagerController {
    private final Logger log = LoggerFactory.getLogger(OrchestratorManagerController.class);
    private OrchestrationProcessService orchestrationProcessService;

    public OrchestratorManagerController(OrchestrationProcessService orchestrationProcessService) {
        this.orchestrationProcessService = orchestrationProcessService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> nuovaOrchestrazione(@Valid  @RequestBody CreateOrchProcessRequest dto){
        log.atDebug().setMessage("Richiesta salvataggio inizio nuova orchestrazione: {}").addArgument(dto).log();
        orchestrationProcessService.createNewProcess(dto);
        log.atDebug().setMessage("Orchestrazione {} salvata")
                .addArgument(dto.uuid()).log();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();

    }
    @PutMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> aggiornaStatoOrchestrazione(@Valid  @RequestBody UpdateOrchProcessRequest dto){
        log.atDebug().setMessage("Richiesta aggiornamento orchestrazione: {}").addArgument(dto).log();
        orchestrationProcessService.aggiornaStatoProcesso(dto);
        log.atDebug().setMessage("Orchestrazione {} aggiornata")
                .addArgument(dto.uuid()).log();

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }

    @GetMapping(value = "/{uuid}/bs-status",
    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BusinessStatusResponse> getBusinessState(@PathVariable("uuid") @NotBlank String uuid){
        BusinessState bs = orchestrationProcessService.getBusinessState(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new BusinessStatusResponse(uuid,bs));
    }

    @GetMapping(value = "/{uuid}/result",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessResultResponse> getResult(@PathVariable("uuid") @NotBlank String uuid){
        String result = orchestrationProcessService.getResult(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ProcessResultResponse(uuid,result));
    }

    @GetMapping(value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProcessDetailResponse> getDettaglio(@PathVariable("uuid") @NotBlank String uuid){
        ProcessDetailResponse result = orchestrationProcessService.getDetail(uuid);
        return ResponseEntity.status(HttpStatus.OK)
                .body(result);
    }
}
