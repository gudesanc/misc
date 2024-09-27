package org.gds.poc.orch.ssm.libreria;

public record DTOEvent(String uuid, String event, String jsonBusinessContext, BusinessState businessStatus) {

}
