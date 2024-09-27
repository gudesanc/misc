package org.gds.poc.orch.manager;

import jakarta.persistence.*;
import org.gds.poc.orch.manager.library.BusinessState;
import org.gds.poc.orch.manager.library.ProcessType;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORC_PROCESS")
public class OrchestrationProcess {

    @Id
    @Column(name = "UUID", nullable = false, length = 255)
    private String uuid;
    @Version
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "TENANT_ID", nullable = false, length = 255)
    private String tenantId;

    @Column(name = "ENTE", nullable = false, length = 255)
    private String ente;

    @Column(name = "START_USER", nullable = false, length = 255)
    private String user;

    @Column(name = "AREA", nullable = false, length = 255)
    private String area;

    @Column(name = "PROCEDURA", nullable = false, length = 255)
    private String procedura;

    @Column(name = "SSM_MACHINE_ID", nullable = false, length = 255)
    private String ssmMachineId;

    @Column(name = "DOMAIN_ORCH_ENDPOINT", nullable = false, length = 255)
    private String domainOrchEndpoint;

    @Column(name = "SSM_STATE", nullable = false, length = 255)
    private String stateMachinCurrentState;

    @Column(name = "START_TIME", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "END_TIME")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "PROCESS_TYPE", nullable = false, length = 50)
    private ProcessType processType;

    @Enumerated(EnumType.STRING)
    @Column(name = "BUSINESS_STATE", nullable = false, length = 50)
    private BusinessState businessState;

    @Column(name = "PARENT_UUID", length = 255)
    private String parentUuid;

    @Column(name = "CONTEXT")
    @Lob
    private String context;

    @Column(name = "PROCESS_RESULT")
    @Lob
    private String result;

    public OrchestrationProcess() {
    }

    public OrchestrationProcess(String uuid, String tenantId, String ente, String user, String area, String procedura, String ssmMachineId, String domainOrchEndpoint, String stateMachinCurrentState, LocalDateTime startTime, ProcessType processType, BusinessState businessState, String parentUuid, String context) {
        this.uuid = uuid;
        this.tenantId = tenantId;
        this.ente = ente;
        this.user = user;
        this.area = area;
        this.procedura = procedura;
        this.ssmMachineId = ssmMachineId;
        this.domainOrchEndpoint = domainOrchEndpoint;
        this.stateMachinCurrentState = stateMachinCurrentState;
        this.startTime = startTime;
        this.processType = processType;
        this.businessState = businessState;
        this.parentUuid = parentUuid;
        this.context = context;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setBusinessState(BusinessState businessState) {
        this.businessState = businessState;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStateMachinCurrentState(String stateMachinCurrentState) {
        this.stateMachinCurrentState = stateMachinCurrentState;
    }

    public String getUuid() {
        return uuid;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getEnte() {
        return ente;
    }

    public String getUser() {
        return user;
    }

    public String getArea() {
        return area;
    }

    public String getProcedura() {
        return procedura;
    }

    public String getSsmMachineId() {
        return ssmMachineId;
    }

    public String getDomainOrchEndpoint() {
        return domainOrchEndpoint;
    }

    public String getStateMachinCurrentState() {
        return stateMachinCurrentState;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public BusinessState getBusinessState() {
        return businessState;
    }

    public String getParentUuid() {
        return parentUuid;
    }

    public String getContext() {
        return context;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "OrchestrationProcess{" +
                "uuid='" + uuid + '\'' +
                ", tenantId='" + tenantId + '\'' +
                ", ente='" + ente + '\'' +
                ", user='" + user + '\'' +
                ", area='" + area + '\'' +
                ", procedura='" + procedura + '\'' +
                ", ssmMachineId='" + ssmMachineId + '\'' +
                ", domainOrchEndpoint='" + domainOrchEndpoint + '\'' +
                ", stateMachinCurrentState='" + stateMachinCurrentState + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", processType=" + processType +
                ", businessState=" + businessState +
                ", parentUuid='" + parentUuid + '\'' +
                ", context='" + context + '\'' +
                ", result='" + result + '\'' +
                '}';
    }
}
