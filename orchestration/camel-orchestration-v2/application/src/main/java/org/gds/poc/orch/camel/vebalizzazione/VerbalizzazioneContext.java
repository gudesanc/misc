package org.gds.poc.orch.camel.vebalizzazione;

import org.gds.pkg.orch.camel.OrchestrationContext;
import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.camel.client.verbale.DtoCreaVerbale;

/**
 * Contesto per la gestione del processo di verbalizzazione
 */
public class VerbalizzazioneContext implements OrchestrationContext {
    private  String uuid;
    private String idVerbale;
    private String oggettoVerbale;
    private DtoProtocollo protocollo;


    @Override
    public String toString() {
        return "VerbalizzazioneContext{" +
                "uuid='" + uuid + '\'' +
                ", idVerbale='" + idVerbale + '\'' +
                ", oggettoVerbale='" + oggettoVerbale + '\'' +
                ", protocollo=" + protocollo +
                '}';
    }

    @Override
    public void uuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIdVerbale() {
        return idVerbale;
    }

    public void setIdVerbale(String idVerbale) {
        this.idVerbale = idVerbale;
    }

    public String getOggettoVerbale() {
        return oggettoVerbale;
    }

    public void setOggettoVerbale(String oggettoVerbale) {
        this.oggettoVerbale = oggettoVerbale;
    }

    public DtoProtocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(DtoProtocollo protocollo) {
        this.protocollo = protocollo;
    }

    public String getUuid() {
        return uuid;
    }
}
