package org.gds.poc.orch.ssm.notificazione;

import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;

/**
 * Contesto per la gestione del processo di verbalizzazione
 */
public class NotificazioneContext {
    private String idNotifica;
    private String oggettoNotifica;
    private DtoProtocollo protocollo;

    @Override
    public String toString() {
        return "NotificazioneContext{" +
                "idNotifica='" + idNotifica + '\'' +
                ", oggettoNotifica='" + oggettoNotifica + '\'' +
                ", protocollo=" + protocollo +
                '}';
    }

    public String getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(String idNotifica) {
        this.idNotifica = idNotifica;
    }

    public String getOggettoNotifica() {
        return oggettoNotifica;
    }

    public void setOggettoNotifica(String oggettoNotifica) {
        this.oggettoNotifica = oggettoNotifica;
    }

    public DtoProtocollo getProtocollo() {
        return protocollo;
    }

    public void setProtocollo(DtoProtocollo protocollo) {
        this.protocollo = protocollo;
    }
}
