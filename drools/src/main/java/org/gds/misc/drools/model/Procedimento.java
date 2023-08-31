package org.gds.misc.drools.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Procedimento implements Serializable {
    private static final long serialVersionUID = 1L;
    private String xml;
    private TipoPratica tipoPratica;
    private List<Errore> errori = new ArrayList<>();
    private List<Warning> warnings = new ArrayList<>();

    @Override
    public String toString() {
        return "Procedimento{" +
                "xml='...'" +
                ", tipoPratica=" + tipoPratica +
                ", errori=" + errori +
                ", warnings=" + warnings +
                '}';
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public TipoPratica getTipoPratica() {
        return tipoPratica;
    }

    public void setTipoPratica(TipoPratica tipoPratica) {
        this.tipoPratica = tipoPratica;
    }

    public List<Errore> getErrori() {
        return errori;
    }

    public void addErroreDichiarazione(String descrizione, String codiceIntervento, String codiceDichiarazione) {
        Errore errore = new Errore(Livello.DICHIRAZIONE,descrizione,codiceIntervento,codiceDichiarazione,null);
        this.errori.add(errore);
    }

    public List<Warning> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<Warning> warnings) {
        this.warnings = warnings;
    }
}
