package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import reactor.core.publisher.Mono;


public interface VerbaleService {
    Mono<DtoVerbale> createVerbale(String oggetto);
    Mono<DtoVerbale> consolidaVerbale(String idVerbale, Protocollo protocollo);
    Mono<DtoVerbale> annullaVerbale(String idVerbale);

}
