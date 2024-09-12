package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


public interface VerbaleService {
    Mono<String> createVerbale(String oggetto);
    Mono<Void> consolidaVerbale(String idVerbale, Protocollo protocollo);
    Mono<Void> annullaVerbale(String idVerbale);
}
