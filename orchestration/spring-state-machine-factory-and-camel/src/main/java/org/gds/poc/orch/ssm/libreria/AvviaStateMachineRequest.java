package org.gds.poc.orch.ssm.libreria;

public record AvviaStateMachineRequest<T>(String intialState, String machineId,  String processName, String endpoint,T businessCtx, ProcessType processType, String parentUUID) {


}
