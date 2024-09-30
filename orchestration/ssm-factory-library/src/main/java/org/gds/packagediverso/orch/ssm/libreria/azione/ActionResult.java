package org.gds.packagediverso.orch.ssm.libreria.azione;

import org.gds.packagediverso.orch.ssm.libreria.BusinessState;

public record ActionResult<T,R>(
        String uuid,
        String evento,
        T businessContext,
        BusinessState nuovoBusinessStatus,
        R risultatoProcesso) {

    public ActionResult(String uuid, String evento, T businessContext, BusinessState nuovoBusinessStatus) {
        this(uuid, evento, businessContext, nuovoBusinessStatus, null);
    }
}