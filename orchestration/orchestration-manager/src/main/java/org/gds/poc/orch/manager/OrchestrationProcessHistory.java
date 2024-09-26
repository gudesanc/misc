package org.gds.poc.orch.manager;

import jakarta.persistence.*;
import org.gds.poc.orch.manager.library.BusinessState;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "ORC_PROCESS_HISTORY")
public class OrchestrationProcessHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "PROCESS_UUID", nullable = false)
    private String processUUID;

    @Column(name = "EVENT_TIME", nullable = false)
    private LocalDateTime eventTime;

    @Column(name = "BS_SOURCE_STATE", nullable = false, length = 255)
    private BusinessState businessStateSource;

    @Column(name = "BS_TARGET_STATE", nullable = false, length = 255)
    private BusinessState businessStateTarget;

    @Column(name = "SM_SOURCE_STATE", nullable = false, length = 255)
    private String stateMachineSourceState;

    @Column(name = "SM_TARGET_STATE", nullable = false, length = 255)
    private String stateMachineTargetState;

    public OrchestrationProcessHistory() {
    }

    public OrchestrationProcessHistory(String processUUID, LocalDateTime eventTime, BusinessState businessStateSource, BusinessState businessStateTarget, String stateMachineSourceState, String stateMachineTargetState) {
        this.processUUID = processUUID;
        this.eventTime = eventTime;
        this.businessStateSource = businessStateSource;
        this.businessStateTarget = businessStateTarget;
        this.stateMachineSourceState = stateMachineSourceState;
        this.stateMachineTargetState = stateMachineTargetState;
    }

    public Integer getId() {
        return id;
    }

    public String getProcessUUID() {
        return processUUID;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public BusinessState getBusinessStateSource() {
        return businessStateSource;
    }

    public BusinessState getBusinessStateTarget() {
        return businessStateTarget;
    }

    public String getStateMachineSourceState() {
        return stateMachineSourceState;
    }

    public String getStateMachineTargetState() {
        return stateMachineTargetState;
    }

    @Override
    public String toString() {
        return "OrchestrationProcessHistory{" +
                "id=" + id +
                ", processUUID='" + processUUID + '\'' +
                ", eventTime=" + eventTime +
                ", businessStateSource=" + businessStateSource +
                ", businessStateTarget=" + businessStateTarget +
                ", stateMachineSourceState='" + stateMachineSourceState + '\'' +
                ", stateMachineTargetState='" + stateMachineTargetState + '\'' +
                '}';
    }
}
