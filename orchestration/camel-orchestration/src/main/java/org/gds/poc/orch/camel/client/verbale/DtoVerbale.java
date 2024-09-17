package org.gds.poc.orch.camel.client.verbale;

import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;

public record DtoVerbale(String id, String oggetto, DtoProtocollo protocollo, StatoVerbale stato){
}
