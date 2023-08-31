package org.gds.misc.drools.units;

import io.quarkus.logging.Log;
import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;
import org.gds.misc.drools.model.Errore;
import org.gds.misc.drools.model.TipoPratica;

public class ValidazioneUnit implements RuleUnitData {
    private String xml;
    private TipoPratica tipoPratica;
    private DataStore<Errore> errori;


    public ValidazioneUnit() {
        this(DataSource.createStore());
    }

    public ValidazioneUnit(DataStore<Errore> errori) {
        this.errori = errori;
    }


    public DataStore<Errore> getErrori() {
        Log.info("Ottengo errori: "+errori);
        return errori;
    }

    public String getXml() {
        return xml;
    }

    public TipoPratica getTipoPratica() {
        return tipoPratica;
    }

    public String leggi(String x){
        Log.info("Leggo: "+x+"... da "+xml);
        return x;
    }
}
