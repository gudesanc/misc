package org.gds.poc.orch.camel.vebalizzazione;

import jakarta.annotation.Resource;
import org.apache.camel.ProducerTemplate;
import org.gds.poc.orch.camel.client.verbale.DtoCreaVerbale;
import org.gds.poc.orch.camel.client.verbale.VerbaleService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/verbalizzazioni")
public class VerbalizzazioneController{

    @Resource
    private final VerbaleService verbaleService;
    @Resource
    private ProducerTemplate producerTemplate;
    public VerbalizzazioneController(VerbaleService verbaleService, ProducerTemplate producerTemplate) {
        this.verbaleService = verbaleService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DtoAvvioProcesso> verbalizza(DtoCreaVerbale verbaleDaVerbalizzare){
        final DtoAvvioProcesso procInfo = new DtoAvvioProcesso();
        return verbaleService.creaVerbale(verbaleDaVerbalizzare)
                .doOnSuccess( v -> {
                    VerbalizzazioneContext ctx = new VerbalizzazioneContext();
                    ctx.setIdVerbale(v.id());
                    ctx.setOggettoVerbale(verbaleDaVerbalizzare.oggetto());
                    String uuid = UUID.randomUUID().toString();;
                    procInfo.setIdVerbale(v.id());
                    procInfo.setUuidProcesso(uuid);
                    producerTemplate.
                            sendBodyAndHeader("direct:start-verbalizzazione",
                                    ctx,"X-UUID-OPERAZIONE",uuid);
                })
                .then(Mono.defer(()->  Mono.just(procInfo)));
    }


}