package org.gds.poc.orch.camel.client.verbale;

import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
import reactor.core.publisher.Mono;

public interface VerbaleService {
    Mono<DtoVerbale> creaVerbale(DtoCreaVerbale infoCreazione);
    Mono<DtoVerbale> consolidaVerbale(String idVerbale, DtoProtocollo protocollo);
    Mono<DtoVerbale> annullaVerbale(String idVerbale);
}
