package org.gds.misc.drools.units;

import org.drools.ruleunits.api.RuleUnitData;
import org.gds.misc.drools.model.validazione.Validazione;

public class ValidazioneUnit extends AbstractRuleUnit implements RuleUnitData {
    private Validazione validazione = new Validazione();
    public Validazione getValidazione(){
        return validazione;
    }
}
