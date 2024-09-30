package org.gds.poc.orch.ssm.client.protocollo;

public interface ProtocolloService {
    DtoProtocollo protocolla(DtoCreaProtocollo infoCreazione);
    void annullaProtocollo(DtoProtocollo protocollo);
}
