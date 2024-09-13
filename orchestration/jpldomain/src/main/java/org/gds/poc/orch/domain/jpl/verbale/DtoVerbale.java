package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;

public record DtoVerbale(String id, String oggetto, Protocollo protocollo,StatoVerbale stato){
}
