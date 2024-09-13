package org.gds.poc.orch.ssm.libreria;

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
    private String jsonSupportData;
    private BusinessStatus businessStatus;

    public GenericStateMachineProcess(String businessId, String processName, String currentState, String machineId, String endPointManager, ProcessType processType, String parentUUID, String jsonSupportData, BusinessStatus businessStatus) {
        this.businessId = businessId;
        this.processName = processName;
        this.currentState = currentState;
        this.machineId = machineId;
        this.endPointManager = endPointManager;
        this.processType = processType;
        this.parentUUID = parentUUID;
        this.jsonSupportData = jsonSupportData;
        this.businessStatus = businessStatus;
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

    public String getJsonSupportData() {
        return jsonSupportData;
    }

    public BusinessStatus getBusinessStatus() {
        return businessStatus;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public void setJsonSupportData(String jsonSupportData) {
        this.jsonSupportData = jsonSupportData;
    }

    public void setBusinessStatus(BusinessStatus businessStatus) {
        this.businessStatus = businessStatus;
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
                ", jsonSupportData='" + jsonSupportData + '\'' +
                ", businessStatus=" + businessStatus +
                '}';
    }
}
