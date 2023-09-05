package org.gds.misc.drools.model.validazione;

import java.util.ArrayList;
import java.util.List;

public class Validazione {
    private List<Errore> errori = new ArrayList<>();
    private List<Warning> warnings = new ArrayList<>();

    public void addErroreProcedimento(String descrizione){
        errori.add(new Errore(Livello.PROCEDIMENTO,descrizione,null,null,null));
    }
    public void addErroreIntevernto(String descrizione, String codIntervento){
        errori.add(new Errore(Livello.INTERVENTO,descrizione,codIntervento,null,null));
    }
    public void addErroreDichiarazione(String descrizione, String codIntervento, String codDichiarazione){
        errori.add(new Errore(Livello.INTERVENTO,descrizione,codIntervento,codDichiarazione,null));
    }

    public void addWarningProcedimento(String descrizione){
        warnings.add(new Warning(Livello.PROCEDIMENTO,descrizione,null,null,null));
    }
    public void addWarningIntevernto(String descrizione, String codIntervento){
        warnings.add(new Warning(Livello.INTERVENTO,descrizione,codIntervento,null,null));
    }
    public void addWarningDichiarazione(String descrizione, String codIntervento, String codDichiarazione){
        warnings.add(new Warning(Livello.INTERVENTO,descrizione,codIntervento,codDichiarazione,null));
    }

    public List<Errore> getErrori() {
        return errori;
    }

    public List<Warning> getWarnings() {
        return warnings;
    }
}
