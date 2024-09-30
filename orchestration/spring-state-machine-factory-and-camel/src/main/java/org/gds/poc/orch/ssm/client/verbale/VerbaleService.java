package org.gds.poc.orch.ssm.client.verbale;

import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;

public interface VerbaleService {
    DtoVerbale creaVerbale(DtoCreaVerbale infoCreazione);
    DtoVerbale consolidaVerbale(String idVerbale, DtoProtocollo protocollo);
    DtoVerbale annullaVerbale(String idVerbale);
}
