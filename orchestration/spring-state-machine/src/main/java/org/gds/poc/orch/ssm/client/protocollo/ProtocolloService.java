package org.gds.poc.orch.ssm.client.protocollo;

import reactor.core.publisher.Mono;

public interface ProtocolloService {
    Mono<DtoProtocollo> protocolla(DtoCreaProtocollo infoCreazione);
    Mono<Void> annullaProtocollo(DtoProtocollo protocollo);
}
