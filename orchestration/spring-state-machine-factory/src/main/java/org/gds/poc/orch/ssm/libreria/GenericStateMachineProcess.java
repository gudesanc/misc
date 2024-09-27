package org.gds.poc.orch.ssm.libreria;

import java.util.ArrayList;
import java.util.List;

/**
 * Processo gereric
 */
public class GenericStateMachineProcess {
    private String uuid;
    private String businessId;
    private String processName;
    private String currentState;
    private String machineId;
    private String endPointManager;
    private ProcessType processType;
    private String parentUUID;
    private BusinessState businessStatus;
    private String jsonBusinessContext;
    private List<String> history = new ArrayList<>();

    public GenericStateMachineProcess(String businessId, String processName, String currentState, String machineId, String endPointManager, ProcessType processType, String parentUUID, String jsonBusinessContext, BusinessState businessStatus) {
        this.businessId = businessId;
        this.processName = processName;
        this.currentState = currentState;
        this.machineId = machineId;
        this.endPointManager = endPointManager;
        this.processType = processType;
        this.parentUUID = parentUUID;
        this.jsonBusinessContext = jsonBusinessContext;
        this.businessStatus = businessStatus;
        history.addLast(currentState);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public String getBusinessId() {
        return businessId;
    }

    public String getProcessName() {
        return processName;
    }

    public String getCurrentState() {
        return currentState;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getEndPointManager() {
        return endPointManager;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public String getParentUUID() {
        return parentUUID;
    }


    public BusinessState getBusinessStatus() {
        return businessStatus;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
        history.addLast(currentState);
    }

    public String getJsonBusinessContext() {
        return jsonBusinessContext;
    }

    public void setJsonBusinessContext(String jsonBusinessContext) {
        this.jsonBusinessContext = jsonBusinessContext;
    }

    public void setBusinessStatus(BusinessState businessStatus) {
        this.businessStatus = businessStatus;
    }

    public List<String> getHistory() {
        return history.stream().toList();
    }

    @Override
    public String toString() {
        return "GenericStateMachineProcess{" +
                "uuid='" + uuid + '\'' +
                ", businessId='" + businessId + '\'' +
                ", processName='" + processName + '\'' +
                ", currentState='" + currentState + '\'' +
                ", machineId='" + machineId + '\'' +
                ", endPointManager='" + endPointManager + '\'' +
                ", processType=" + processType +
                ", parentUUID='" + parentUUID + '\'' +
                ", jsonBusinessContext='" + jsonBusinessContext + '\'' +
                ", businessStatus=" + businessStatus +
                '}';
    }
}
