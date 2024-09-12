package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class VerbaleServiceImpl implements VerbaleService{
    private static final Map<String, Verbale> verbali = new HashMap();
    @Override
    public Mono<String> createVerbale(String oggetto) {
        String uuid = UUID.randomUUID().toString();
        Verbale v = new Verbale(uuid,oggetto);
        verbali.put(uuid,v);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return Mono.error(new RuntimeException(e));
        }
        return Mono.just(uuid);
    }

    @Override
    public Mono<Void> consolidaVerbale(String idVerbale, final Protocollo protocollo) {
        load(idVerbale)
                .subscribe(v -> v.consolida(protocollo));
        return Mono.empty();
    }

    @Override
    public Mono<Void> annullaVerbale(String idVerbale) {
        load(idVerbale).
                subscribe(Verbale::annulla)
        ;
        return Mono.empty();
    }

    private Mono<Verbale> load(String idVerbale){
        Verbale v = verbali.get(idVerbale);
        if(v==null){
            return Mono.error(new IllegalArgumentException("Verbale "+idVerbale+" non presente"));
        }
        return Mono.just(v);
    }
}
