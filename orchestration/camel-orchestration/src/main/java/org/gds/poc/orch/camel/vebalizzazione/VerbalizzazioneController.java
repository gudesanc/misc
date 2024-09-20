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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
        String uuid = UUID.randomUUID().toString();;
        Map<String, Object> headers = new HashMap<>();
        headers.put(VerbalizzazioneRoute.X_UUID_OPERAZIONE,uuid);
        producerTemplate.sendBodyAndHeaders("seda:start-verbalizzazione",
                verbaleDaVerbalizzare,
                headers
        );
        return Mono.just(new DtoAvvioProcesso(uuid));
//        return verbaleService.creaVerbale(verbaleDaVerbalizzare)
//                .doOnSuccess( v -> {
//
//                    ctx.setIdVerbale(v.id());
//                    ctx.setOggettoVerbale(verbaleDaVerbalizzare.oggetto());
//                    String uuid = UUID.randomUUID().toString();;
//                    procInfo.setIdVerbale(v.id());
//                    procInfo.setUuidProcesso(uuid);
//                    producerTemplate.
//                            sendBodyAndHeader("seda:start-verbalizzazione",
//                                    ctx,"X-UUID-OPERAZIONE",uuid);
//                })
//                .then(Mono.defer(()->  Mono.just(procInfo)));
    }


}