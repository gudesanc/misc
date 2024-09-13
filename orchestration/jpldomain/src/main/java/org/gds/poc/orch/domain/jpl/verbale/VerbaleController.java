package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(path = "/verbali")
public class VerbaleController {
    private final Logger log = LoggerFactory.getLogger(VerbaleController.class);
    private final VerbaleService verbaleService;

    public VerbaleController(VerbaleService verbaleService) {
        this.verbaleService = verbaleService;
    }

    @PostMapping(value = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus()
    public Mono<ResponseEntity<Mono<DtoVerbale>>> creaVerbale(@RequestBody DtoCreaVerbale dto){
        log.atInfo().setMessage("Richiesta creazione verbale, oggetto: {}").addArgument(dto).log();
        return Mono.just(
                ResponseEntity.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(verbaleService.createVerbale(dto.oggetto()))
        );

    }

    @PostMapping(value = "/{id}/protocollo",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<DtoVerbale> consolidaVerbale(@PathVariable String id, @RequestBody Protocollo protocollo){
        log.atInfo().setMessage("Richiesta considamento verbale, id verbale: {}, protocollo: {}")
                .addArgument(id)
                .addArgument(protocollo)
                .log();
        return verbaleService.consolidaVerbale(id,protocollo);
    }
    @PostMapping("/{id}/annulla")
    public Mono<DtoVerbale> annullaVerbale(@PathVariable String id){
        log.atInfo().setMessage("Richiesta anullamento verbale, id verbale: {}")
                .addArgument(id)
                .log();
        return verbaleService.annullaVerbale(id);
    }
}
