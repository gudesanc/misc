package org.gds.poc.orch.ssm.libreria;

public record ActionResult<T>(String uuid, String evento, T businessContext, BusinessStatus nuovoBusinessStatus) {
}
