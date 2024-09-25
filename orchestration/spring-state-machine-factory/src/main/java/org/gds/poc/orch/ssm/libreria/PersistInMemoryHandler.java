//package org.gds.poc.orch.ssm.libreria;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
//import org.springframework.statemachine.state.State;
//import org.springframework.statemachine.transition.Transition;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import java.util.UUID;
//
//public class PersistInMemoryHandler {
//    private final Logger log = LoggerFactory.getLogger(PersistInMemoryHandler.class);
//
//    public static final String X_UUID_GENERIC = "x-uuid-generic";
//    public static final String X_BUSINESS_CTX = "x-business-ctx";
//    public static final String X_BUSINESS_STATUS = "x-business-status";
//    private final PersistStateMachineHandler handler;
//
//    private final static Map<String, GenericStateMachineProcess> processi = new HashMap<>();
//
//
//    public PersistInMemoryHandler(PersistStateMachineHandler persistStateMachineHandler) {
//        this.handler=persistStateMachineHandler;
//        this.handler.addPersistStateChangeListener( new PersistInMemoryStateChangeListener());
//    }
//
//    public Optional<GenericStateMachineProcess> getInfoProcesso(String uuid){
//        return Optional.ofNullable(processi.get(uuid));
//    }
//
//
//    public <T> String createNewProcess(GenericStateMachineProcess genericStateMachineProcess){
//        String uuid = UUID.randomUUID().toString();
//        genericStateMachineProcess.setUuid(uuid);
//        processi.put(uuid,genericStateMachineProcess);
//        log.atInfo().setMessage("Creato processo: {}").addArgument(genericStateMachineProcess).log();
//        return uuid;
//    }
//
//    public <T> void change(String uuid, String event, BusinessStatus businessStatus, String jsonBusinessContext) {
//        log.atInfo().setMessage("Richiesta di notificare evento {}, con stato business {} e ctx {} al processo uuid: {}")
//                .addArgument(event)
//                .addArgument(businessStatus)
//                .addArgument(jsonBusinessContext)
//                .addArgument(uuid).log()
//        ;
//        GenericStateMachineProcess process = processi.get(uuid);
//        log.atInfo().setMessage("Processo recuperato: {}")
//                .addArgument(process).log()
//        ;
//
//        handler.handleEventWithStateReactively(MessageBuilder
//                        .withPayload(event)
//                        .setHeader(X_UUID_GENERIC, uuid)
//                        .setHeader(X_BUSINESS_CTX,jsonBusinessContext)
//                        .setHeader(X_BUSINESS_STATUS, businessStatus)
//                .build(), process.getCurrentState())
//                .doOnSuccess( p ->
//                        log.atInfo().setMessage("Notificato evento {}, con stato business {} e json {} al processo: {}")
//                                .addArgument(event)
//                                .addArgument(businessStatus)
//                                .addArgument(jsonBusinessContext)
//                                .addArgument(process).log())
//                .doOnError(e ->
//                        log.atInfo().setMessage("Notifica evento {}, al processo: {}, errore: {}")
//                                .addArgument(event)
//                                .addArgument(process)
//                                .addArgument(e)
//                                .log()
//                        )
//                .subscribe();
//    }
//
//    private void printStatus(){
//        System.out.println("\n\n****************************************");
//        processi.values().forEach(p -> {
//            StringBuilder sb = new StringBuilder("\n").append(p.getUuid())
//                    .append(" -- ")
//                            .append(p.getBusinessStatus())
//                                    .append(" -- ")
//                                            .append(p.getCurrentState());
//            p.getHistory().forEach(h ->sb.append("\n\t\t").append(h));
//            System.out.println(sb.toString());
//        });
//        System.out.println("****************************************\n\n\n");
//    }
//
//    private class PersistInMemoryStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {
//        private final Logger log = LoggerFactory.getLogger(PersistInMemoryStateChangeListener.class);
//
//
//        @Override
//        public void onPersist(State<String, String> state, Message<String> message,
//                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
//            log.atInfo().setMessage("Richiesta aggoiramento stato: {}, messaggio {}, transizione {}")
//                    .addArgument(state)
//                    .addArgument(transition)
//                    .addArgument(message).log();
//            if (message != null && message.getHeaders().containsKey(X_UUID_GENERIC)) {
//                String uuid = message.getHeaders().get(X_UUID_GENERIC, String.class);
//                String jsonBusinessContext = message.getHeaders().get(X_BUSINESS_CTX, String.class);
//                BusinessStatus businessStatus = message.getHeaders().get(X_BUSINESS_STATUS, BusinessStatus.class);
//                GenericStateMachineProcess processo = processi.get(uuid);
//                processo.setCurrentState(state.getId());
//                if(businessStatus!=null) {
//                    processo.setBusinessStatus(businessStatus);
//                }
//                if(jsonBusinessContext!=null) {
//                    processo.setJsonBusinessContext(jsonBusinessContext);
//                    log.atInfo().setMessage("BusinessCtx: {}").addArgument(jsonBusinessContext).log();
//                }
//                log.atInfo().setMessage("Aggiornato processo: {}").addArgument(processo).log();
//            }
//            printStatus();
//        }
//    }
//}
