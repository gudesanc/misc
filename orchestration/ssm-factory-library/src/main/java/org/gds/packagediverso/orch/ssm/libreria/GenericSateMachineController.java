package org.gds.packagediverso.orch.ssm.libreria;

import org.gds.packagediverso.orch.ssm.libreria.notifica.CreateOrchProcessRequest;
import org.gds.packagediverso.orch.ssm.libreria.notifica.OrchestrationManagerNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public abstract class GenericSateMachineController<T,R> {
    private static final Logger log = LoggerFactory.getLogger(GenericSateMachineController.class);
    public static final String X_UUID_GENERIC = "x-uuid-generic";
    public static final String X_BUSINESS_CTX = "x-business-ctx";
    public static final String X_BUSINESS_STATUS = "x-business-status";
    public static final String X_PROCESS_RESULT = "x-process-result";
    private StateMachineFactory<String, String> stateMachineFactory;
    @Autowired
    private OrchestrationManagerNotification managerNotification;
    private final Map<String, StateMachine<String, String>> machines = new HashMap<>();


    protected GenericSateMachineController(StateMachineFactory<String, String> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }


    protected String createNewProcess(AvviaStateMachineRequest<T> request, String event){
        String json = null;
        if(request.businessCtx()!=null){
            json = SupportJsonSerializer.serialize(request.businessCtx());
        }
        String uuid = UUID.randomUUID().toString();



        CreateOrchProcessRequest creationNotification = new CreateOrchProcessRequest(
                uuid, LocalDateTime.now(),request.parentUUID(),
                "TENANT_ID", //TODO da ricordarsi,
                "ENTE",
                "USER",
                "AREA",
                "PROCEDURA",
                request.machineId(),
                request.endpoint(),
                request.processType(),
                request.intialState(),json);
        long startTime = System.currentTimeMillis();
        StateMachine<String, String> machine = getMachine(uuid);
        managerNotification.notificaNuovoProcesso(creationNotification);
        //Avviamo la macchia
        machine.startReactively()//.tap(Micrometer.ob)
                .subscribe();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.atInfo().setMessage("Tempo creazione ed avvio state machine: {} (ms)").addArgument(duration).log();
        machine.getExtendedState().getVariables().put(X_UUID_GENERIC, uuid);
        machine.getExtendedState().getVariables().put(X_BUSINESS_CTX,json);
        machine.getExtendedState().getVariables().put(X_BUSINESS_STATUS, BusinessState.RUNNING);
        if(event!=null) {
            //Mandiamo l'evento inizale...
            Message<String> msg = MessageBuilder
                    .withPayload(event)
                    .build();
            machine.sendEvent(Mono.just(msg))
                    .subscribe(s -> {
                        log.atInfo().setMessage("{} ...evento {} inviato")
                                .addArgument(uuid)
                                .addArgument(event)
                                .log();
                    });

        }
        return uuid;
    }

    private synchronized StateMachine<String, String> getMachine(String id) {
        StateMachine<String,String> machine = machines.get(id);
        if (machine == null) {
            machine = stateMachineFactory.getStateMachine(id);
            machines.put(id, machine);
        }
        return machine;
    }


}
