package org.gds.poc.orch.ssm.libreria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



public abstract class GenericSateMachineController<T> {
    private static final Logger log = LoggerFactory.getLogger(GenericSateMachineController.class);
    public static final String X_UUID_GENERIC = "x-uuid-generic";
    public static final String X_BUSINESS_CTX = "x-business-ctx";
    public static final String X_BUSINESS_STATUS = "x-business-status";
    private StateMachineFactory<String, String> stateMachineFactory;
    private final Map<String, StateMachine<String, String>> machines = new HashMap<>();


    protected GenericSateMachineController(StateMachineFactory<String, String> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }


    protected String createNewProcess(AvviaStateMachineRequest request, String event){
        String json = null;
        if(request.businessCtx()!=null){
            json = BusinessContextJsonSerializer.serialize(request.businessCtx());
        }
        String uuid = UUID.randomUUID().toString();
        GenericStateMachineProcess process = new GenericStateMachineProcess(
                uuid,request.processName(),request.intialState(),request.machineId(),request.machineId(),request.processType(),request.machineId(),json,BusinessStatus.RUNNING
        );//va be' questo andrebbe persistito
        process.setUuid(uuid);

        long startTime = System.currentTimeMillis();
        StateMachine<String, String> machine = getMachine(uuid);
        //Avviamo la macchia
        machine.startReactively().subscribe();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        log.atInfo().setMessage("Tempo creazione ed avvio state machine: {} (ms)").addArgument(duration).log();
        machine.getExtendedState().getVariables().put(X_UUID_GENERIC, uuid);
        machine.getExtendedState().getVariables().put(X_BUSINESS_CTX,json);
        machine.getExtendedState().getVariables().put(X_BUSINESS_STATUS, BusinessStatus.RUNNING);

        //Mandiamo l'evento inizale...
        Message<String> msg =MessageBuilder
                .withPayload(event)
//                .setHeader(X_UUID_GENERIC, uuid)
//                .setHeader(X_BUSINESS_CTX,json)
//                .setHeader(X_BUSINESS_STATUS, BusinessStatus.RUNNING)
                .build();
        machine.sendEvent(Mono.just(msg))
                .subscribe(s->{
                    log.atInfo().setMessage("{} ...evento {} inviato")
                            .addArgument(uuid)
                            .addArgument(event)
                            .log();
                });

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

//    protected void notificaEvento(String uuid, String event, T businessContext, BusinessStatus businessStatus){
//        String json = null;
//        if(businessContext!=null){
//            json = BusinessContextJsonSerializer.serialize(businessContext);
//        }
//        persistInMemoryHandler
//                .change(uuid,event,businessStatus,json);
//
//    }
//
//
//    @PostMapping(value="/events", consumes = MediaType.APPLICATION_JSON_VALUE)
//    public Mono<Void> notifica(@RequestBody DTOEvent event){
//        persistInMemoryHandler
//                .change(event.uuid(),event.event(),event.businessStatus(),event.jsonBusinessContext());
//        return Mono.empty();
//    }
}
