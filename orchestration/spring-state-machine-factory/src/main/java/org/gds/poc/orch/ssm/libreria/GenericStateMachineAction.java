package org.gds.poc.orch.ssm.libreria;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;


public abstract class GenericStateMachineAction<T> implements Action<String, String> {
    private static final Logger log = LoggerFactory.getLogger(GenericStateMachineAction.class);

//    private final RestClient webClient;
    private final Class<T> businessCtxClass;

    public GenericStateMachineAction(Class<T> businessCtxClass) {
//
//        this.webClient = webClientBuilder.baseUrl(endpoint)
//                .build();
//        log.atInfo().setMessage("Endpoint servizi notifica: {}").addArgument(endpoint).log();
        this.businessCtxClass = businessCtxClass;
    }


    @Override
    public final void execute(StateContext<String, String> stateContext) {
        String uuid = (String) stateContext.getMessageHeader(GenericSateMachineController.X_UUID_GENERIC);
        String jsonBusinessContext = (String) stateContext.getMessageHeader(GenericSateMachineController.X_BUSINESS_CTX);
        BusinessStatus bs = (BusinessStatus) stateContext.getMessageHeader(GenericSateMachineController.X_BUSINESS_STATUS);
        T businessCtx = null;
        if(jsonBusinessContext!=null){
            businessCtx = BusinessContextJsonSerializer.deserialize(jsonBusinessContext,businessCtxClass);
        }
        ActionResult<T> actionResult = execute(uuid, businessCtx, bs, stateContext);
        StateMachine<String, String> machine = stateContext.getStateMachine();
        String json = null;
        if(actionResult.businessContext()!=null){
            json = BusinessContextJsonSerializer.serialize(actionResult.businessContext());
        }

        //Mandiamo l'evento inizale...
        Message<String> msg = MessageBuilder
                .withPayload(actionResult.evento())
                .setHeader(GenericSateMachineController.X_UUID_GENERIC, uuid)
                .setHeader(GenericSateMachineController.X_BUSINESS_CTX,json)
                .setHeader(GenericSateMachineController.X_BUSINESS_STATUS, actionResult.nuovoBusinessStatus())
                .build();
        machine.sendEvent(Mono.just(msg))
                .subscribe(s->{
                    log.atInfo().setMessage("{} ...evento {} inviato")
                            .addArgument(uuid)
                            .addArgument(actionResult.evento())
                            .log();
                });
    }

    public abstract ActionResult<T> execute(String uuid, T businessCtx, BusinessStatus bs, StateContext<String, String> stateContext);
//
//    protected void notificaEvento(String uuid, String evento, T businessContext, BusinessStatus nuovoBusinessStatus) {
//        log.atInfo().setMessage("Richiesa notifica evento  uuid: {}, evento: {}, business Ctx: {}, businessStatus: {}")
//                .addArgument(uuid).addArgument(evento).addArgument(businessContext).addArgument(nuovoBusinessStatus).log();
//        String jsonBusinessCtx = null;
//        if(businessContext!=null){
//            jsonBusinessCtx = BusinessContextJsonSerializer.serialize(businessContext);
//        }
//        DTOEvent event =new DTOEvent(uuid,evento, jsonBusinessCtx,nuovoBusinessStatus);
//        log.atInfo().setMessage("DTO richiesta generato {}").addArgument(event).log();
//
//        webClient.post().uri("/events")
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(event)
//                .retrieve();
//
//    }
//
//

}
