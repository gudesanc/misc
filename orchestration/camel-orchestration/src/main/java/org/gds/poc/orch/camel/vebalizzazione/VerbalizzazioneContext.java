package org.gds.poc.orch.camel.vebalizzazione;

import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;

/**
 * Contesto per la gestione del processo di verbalizzazione
 */
public class VerbalizzazioneContext {
    private String idVerbale;
    private String oggettoVerbale;
    private DtoProtocollo protocollo;

    @Override
    public String toString() {
        return "VerbalizzazioneContext{" +
                "idVerbale='" + idVerbale + '\'' +
                ", oggettoVerbale='" + oggettoVerbale + '\'' +
                ", protocollo=" + protocollo +
                '}';
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
}
