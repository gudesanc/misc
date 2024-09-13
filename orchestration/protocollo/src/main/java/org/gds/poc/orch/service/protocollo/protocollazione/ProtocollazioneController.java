package org.gds.poc.orch.service.protocollo.protocollazione;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(path = "/protocolli")
public class ProtocollazioneController {
    private final Logger log = LoggerFactory.getLogger(ProtocollazioneController.class);
    private final ProtocollazioneService protocollazioneService;

    public ProtocollazioneController(ProtocollazioneService protocollazioneService) {
        this.protocollazioneService = protocollazioneService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Mono<Protocollo>>> protocolla(@RequestBody DtoCreaProtocollo dto){
        log.atInfo().setMessage("Richiesta creazione protocollo: {}").addArgument(dto).log();
        return Mono.just(
                ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(protocollazioneService.creaProtocollo(dto))
        );

    }
    @DeleteMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<ResponseEntity<Mono<Protocollo>>> annullaProtocolla(@RequestBody Protocollo protocollo){
        log.atInfo().setMessage("Richiesta annullamento protocollo: {}").addArgument(protocollo).log();
        return Mono.just(
                ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(protocollazioneService.annullaProtocollo(protocollo))
        );

    }
}
