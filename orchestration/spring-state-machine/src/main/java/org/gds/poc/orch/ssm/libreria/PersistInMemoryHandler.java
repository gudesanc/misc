package org.gds.poc.orch.ssm.libreria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PersistInMemoryHandler {
    private final Logger log = LoggerFactory.getLogger(PersistInMemoryHandler.class);
    private static final String X_UUID_GENERIC = "x-uuid-generic";
    private static final String X_JSON_SUPPORT = "x-json-support";
    private static final String X_BUSINESS_STATUS = "x-business-status";
    private final PersistStateMachineHandler handler;
    private final static Map<String, GenericStateMachineProcess> processi = new HashMap<>();
    private final PersistStateChangeListener listener = new LocalPersistStateChangeListener();


    public PersistInMemoryHandler(PersistStateMachineHandler persistStateMachineHandler) {
        this.handler=persistStateMachineHandler;
        this.handler.addPersistStateChangeListener(listener);
    }

    public String createNewProcess(GenericStateMachineProcess genericStateMachineProcess){
        String uuid = UUID.randomUUID().toString();
        genericStateMachineProcess.setUuid(uuid);
        processi.put(uuid,genericStateMachineProcess);
        log.atInfo().setMessage("Creato processo: {}").addArgument(genericStateMachineProcess).log();
        return uuid;
    }

    public void change(String uuid, String event, BusinessStatus businessStatus, String jsonSupportData) {
        GenericStateMachineProcess process = processi.get(uuid);
        handler.handleEventWithStateReactively(MessageBuilder
                        .withPayload(event)
                        .setHeader(X_UUID_GENERIC, uuid)
                        .setHeader(X_JSON_SUPPORT,jsonSupportData)
                        .setHeader(X_BUSINESS_STATUS, businessStatus)
                .build(), process.getCurrentState())
                .doOnSuccess( p ->
                        log.atInfo().setMessage("Notificato evento {}, con stato business {} e json {} al processo: {}")
                                .addArgument(event)
                                .addArgument(businessStatus)
                                .addArgument(jsonSupportData)
                                .addArgument(process).log()
                )
        ;
    }


    private class LocalPersistStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {
        private final Logger log = LoggerFactory.getLogger(LocalPersistStateChangeListener.class);

        @Override
        public void onPersist(State<String, String> state, Message<String> message,
                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
            if (message != null && message.getHeaders().containsKey(X_UUID_GENERIC)) {
                String uuid = message.getHeaders().get(X_UUID_GENERIC, String.class);
                String jsonSupport = message.getHeaders().get(X_JSON_SUPPORT, String.class);
                BusinessStatus businessStatus = message.getHeaders().get(X_BUSINESS_STATUS, BusinessStatus.class);
                GenericStateMachineProcess processo = processi.get(uuid);
                processo.setCurrentState(state.getId());
                if(businessStatus!=null) {
                    processo.setBusinessStatus(businessStatus);
                }
                processo.setJsonSupportData(jsonSupport);
                log.atInfo().setMessage("Aggiornato processo: {}").addArgument(processo).log();
            }
        }
    }
}
