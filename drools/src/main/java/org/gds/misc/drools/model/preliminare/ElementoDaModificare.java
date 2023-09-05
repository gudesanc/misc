package org.gds.misc.drools.model.preliminare;

public interface ElementoDaModificare {
    public enum TipoElemento{
        INTERVENTO,DICHIRAZIONE
    }
    public enum Azione{
        RIMUOVERE
    }
    TipoElemento getTipo();
    Azione getAzione();
}
