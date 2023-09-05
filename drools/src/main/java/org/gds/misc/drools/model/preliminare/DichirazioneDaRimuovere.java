package org.gds.misc.drools.model.preliminare;

public record DichirazioneDaRimuovere(String codiceIntervento, String codiceDichiarazione) implements ElementoDaModificare {
    @Override
    public TipoElemento getTipo() {
        return TipoElemento.DICHIRAZIONE;
    }

    @Override
    public Azione getAzione() {
        return Azione.RIMUOVERE;
    }

}
