package org.gds.poc.orch.ssm.libreria;

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