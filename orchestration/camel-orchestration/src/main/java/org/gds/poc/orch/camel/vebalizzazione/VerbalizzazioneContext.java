package org.gds.poc.orch.camel.vebalizzazione;

import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.camel.client.verbale.DtoCreaVerbale;

/**
 * Contesto per la gestione del processo di verbalizzazione
 */
public class VerbalizzazioneContext {
    private final String uuid;
    private String idVerbale;
    private String oggettoVerbale;
    private DtoProtocollo protocollo;

    public VerbalizzazioneContext(String uuid) {
        if(uuid==null){
            throw new NullPointerException("UUID non può essere null sul contesto dell'orchestrazione");
        }
        this.uuid = uuid;
    }


    @Override
    public String toString() {
        return "VerbalizzazioneContext{" +
                "uuid='" + uuid + '\'' +
                ", idVerbale='" + idVerbale + '\'' +
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

    public String getUuid() {
        return uuid;
    }
}
