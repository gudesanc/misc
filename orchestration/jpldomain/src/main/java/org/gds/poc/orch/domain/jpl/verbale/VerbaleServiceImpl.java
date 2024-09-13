package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class VerbaleServiceImpl implements VerbaleService{
    private final Logger log = LoggerFactory.getLogger(VerbaleServiceImpl.class);
    private static final Map<String, Verbale> verbali = new HashMap();
    @Override
    public Mono<DtoVerbale> createVerbale(String oggetto) {
        String uuid = UUID.randomUUID().toString();
        Verbale v = new Verbale(uuid,oggetto);
        verbali.put(uuid,v);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return Mono.error(new RuntimeException(e));
        }
        return Mono.just(v.toDtoVerbale());
    }

    @Override
    public Mono<DtoVerbale> consolidaVerbale(final String idVerbale, final Protocollo protocollo) {
        return load(idVerbale).
                doOnNext(v -> v.consolida(protocollo)).
                doOnNext(v -> log.atInfo()
                        .setMessage("Verbale [id: {}] consolidato con protcollo {}")
                        .addArgument(idVerbale)
                        .addArgument(protocollo)
                        .log()
                ).
                doOnError(e -> log.atError()
                        .setMessage("Consolidamento verbale [id: {}] fallito: {}")
                        .addArgument(idVerbale)
                        .addArgument(e.getMessage())
                        .log()
                ).
                map(Verbale::toDtoVerbale);
    }

    @Override
    public Mono<DtoVerbale> annullaVerbale(String idVerbale) {
        return load(idVerbale).
                doOnNext(Verbale::annulla).
                doOnNext(v -> log.atInfo()
                        .setMessage("Verbale [id: {}] annullato")
                        .addArgument(idVerbale)
                        .log()
                ).
                doOnError(e -> log.atError()
                                .setMessage("Annullamento verbale [id: {}] fallito: {}")
                                .addArgument(idVerbale)
                                .addArgument(e.getMessage())
                                .log()).
                map(Verbale::toDtoVerbale);

    }

    private Mono<Verbale> load(String idVerbale){
        Verbale v = verbali.get(idVerbale);
        if(v==null){
            throw new IllegalArgumentException("Verbale "+idVerbale+" non presente");
        }
        return Mono.just(v);
    }
}
