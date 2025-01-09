package org.gds.poc.orch.camel.vebalizzazione;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.apache.camel.ProducerTemplate;
import org.gds.poc.orch.camel.client.verbale.DtoCreaVerbale;
import org.gds.poc.orch.camel.client.verbale.VerbaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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


    private static final Logger log = LoggerFactory.getLogger(VerbalizzazioneController.class);
    @Resource
    private final VerbaleService verbaleService;
    @Resource
    private ProducerTemplate producerTemplate;
    public VerbalizzazioneController(VerbaleService verbaleService, ProducerTemplate producerTemplate) {
        this.verbaleService = verbaleService;
    }

    @GetMapping(value = "/stop")
    public Mono<String> stop(){
        System.exit(3);
        return Mono.just("Addio :)");
    }


    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DtoAvvioProcesso> verbalizza(DtoCreaVerbale verbaleDaVerbalizzare){
        String uuid = UUID.randomUUID().toString();;
        Map<String, Object> headers = new HashMap<>();
        headers.put(VerbalizzazioneRoute.HEAD_UUID,uuid);
        log.atInfo().setMessage("inizio avvio processo per {}").addArgument(verbaleDaVerbalizzare).log();
        producerTemplate.sendBodyAndHeaders("seda:start-verbalizzazione",
                new DtoCreaVerbale("chehe"),
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