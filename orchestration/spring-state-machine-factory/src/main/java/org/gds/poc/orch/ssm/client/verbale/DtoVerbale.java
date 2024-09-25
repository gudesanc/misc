package org.gds.poc.orch.ssm.client.verbale;

import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;

public record DtoVerbale(String id, String oggetto, DtoProtocollo protocollo, StatoVerbale stato){
}
