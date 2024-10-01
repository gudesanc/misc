package org.gds.poc.orch.service.protocollo.protocollazione;

import org.gds.poc.orch.domain.jpl.common.Protocollo;


public interface ProtocollazioneService {
    Protocollo creaProtocollo(DtoCreaProtocollo dto);

    Protocollo annullaProtocollo(Protocollo p);
}
