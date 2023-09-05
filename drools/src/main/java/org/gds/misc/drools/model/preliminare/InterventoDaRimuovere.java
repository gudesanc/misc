package org.gds.misc.drools.model.preliminare;

public record InterventoDaRimuovere(String codiceIntervento) implements ElementoDaModificare {
    @Override
    public TipoElemento getTipo() {
        return TipoElemento.INTERVENTO;
    }
    @Override
    public Azione getAzione() {
        return Azione.RIMUOVERE;
    }

}
