package org.gds.misc.drools.units;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStream;
import org.drools.ruleunits.api.RuleUnitData;
import org.gds.misc.drools.model.preliminare.ElementoDaModificare;

/**
 * Rule unit che si occupa di verificare le operazioni
 * preliminari alla crezione di una pratica da fare...
 * in particolare se ci sono allegti o interventi da rimuovere
 */
public class InformazioniPreliminariUnit extends AbstractRuleUnit implements RuleUnitData {
    private DataStream<ElementoDaModificare> elementiDaRimuovere = DataSource.createStream();

    public DataStream<ElementoDaModificare> getElementiDaRimuovere() {
        return elementiDaRimuovere;
    }
}
