package org.gds.poc.orch.domain.jpl.verbale;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import jakarta.annotation.Resource;
import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping(path = "/verbali")
public class VerbaleController {
    private final VerbaleService verbaleService;

    public VerbaleController(VerbaleService verbaleService) {
        this.verbaleService = verbaleService;
    }

    @PostMapping("/")
    public Mono<String> creaVerbale(DtoCreaVerbale dto){
        return verbaleService.createVerbale(dto.oggetto());
    }

    @PostMapping("/{id}/protocollo")
    public Mono<Void> consolidaVerbale(@PathVariable String id, @RequestBody Protocollo protocllo){
        return verbaleService.consolidaVerbale(id,protocllo);
    }
}
