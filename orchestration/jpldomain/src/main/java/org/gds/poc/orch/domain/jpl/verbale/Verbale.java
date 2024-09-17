package org.gds.poc.orch.domain.jpl.verbale;

import org.gds.poc.orch.domain.jpl.common.Protocollo;

import javax.xml.stream.events.DTD;
import java.io.Serializable;

public class Verbale {
    private String id;
    private String oggetto;
    private Protocollo protocollo;
    private StatoVerbale stato;

    public Verbale(String id, String oggetto) {
        this.id = id;
        this.oggetto = oggetto;
        stato = StatoVerbale.BOZZA;
    }

    public void consolida(Protocollo p){

        if( ((int)(Math.random()*10000)%2)==0){
            throw new UnsupportedOperationException("...e niente mod 2 non è stato superato");
        }
        if(StatoVerbale.BOZZA.equals(stato)) {
            stato = StatoVerbale.CONSOLIDATO;
            protocollo = p;
        }
        else {
            throw new IllegalStateException("Lo stato del verbale "+id+ " non consente il consolidamento");
        }
    }

    public void annulla(){
        if(!StatoVerbale.CONSOLIDATO.equals(stato)) {
            stato = StatoVerbale.ANNULLATO;
        }
        else {
            throw new IllegalStateException("Lo stato del verbale "+id+ " non consente il consolidamento");
        }
    }

    @Override
    public String toString() {
        return "Verbale{" +
                "id='" + id + '\'' +
                ", oggetto='" + oggetto + '\'' +
                ", protocollo=" + protocollo +
                ", stato=" + stato +
                '}';
    }

    public DtoVerbale toDtoVerbale(){
        return new DtoVerbale(id,oggetto,protocollo,stato);
    }
}
