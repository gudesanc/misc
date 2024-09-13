package org.gds.poc.orch.service.protocollo.protocollazione;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import reactor.core.publisher.Mono;


public interface ProtocollazioneService {
    Mono<Protocollo> creaProtocollo(DtoCreaProtocollo dto);

    Mono<Protocollo> annullaProtocollo(Protocollo p);
}
