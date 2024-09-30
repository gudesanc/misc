package org.gds.packagediverso.orch.ssm.libreria.azione;


import org.gds.packagediverso.orch.ssm.libreria.BusinessState;
import org.gds.packagediverso.orch.ssm.libreria.GenericSateMachineController;
import org.gds.packagediverso.orch.ssm.libreria.SupportJsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import reactor.core.publisher.Mono;


public abstract class GenericStateMachineAction<T,R> implements Action<String, String> {
    private static final Logger log = LoggerFactory.getLogger(GenericStateMachineAction.class);

    private final Class<T> businessCtxClass;
    private final Class<R> resultClass;

    public GenericStateMachineAction(Class<T> businessCtxClass, Class<R> resultClass) {
        this.businessCtxClass = businessCtxClass;
        this.resultClass = resultClass;
    }


    @Override
    public final void execute(StateContext<String, String> stateContext) {
        String uuid = (String) stateContext.getExtendedState().getVariables().get(GenericSateMachineController.X_UUID_GENERIC);
        String jsonBusinessContext = (String) stateContext.getExtendedState().getVariables().get(GenericSateMachineController.X_BUSINESS_CTX);
        BusinessState bs = (BusinessState) stateContext.getExtendedState().getVariables().get(GenericSateMachineController.X_BUSINESS_STATUS);
        T businessCtx = null;
        if(jsonBusinessContext!=null){
            businessCtx = SupportJsonSerializer.deserialize(jsonBusinessContext,businessCtxClass);
        }
        ActionResult<T,R> actionResult = execute(uuid, businessCtx, bs, stateContext);
        StateMachine<String, String> machine = stateContext.getStateMachine();
        String json = null;
        if(actionResult.businessContext()!=null){
            json = SupportJsonSerializer.serialize(actionResult.businessContext());
        }

        stateContext.getExtendedState().getVariables().put(GenericSateMachineController.X_UUID_GENERIC, uuid);
        if(json!=null){
            stateContext.getExtendedState().getVariables().put(GenericSateMachineController.X_BUSINESS_CTX, json);
        }
        if(actionResult.nuovoBusinessStatus()!=null){
            stateContext.getExtendedState().getVariables().put(GenericSateMachineController.X_BUSINESS_STATUS, actionResult.nuovoBusinessStatus());
        }
        if(actionResult.risultatoProcesso()!=null){
            String jsonResult = SupportJsonSerializer.serialize(actionResult.risultatoProcesso());
            stateContext.getExtendedState().getVariables().put(GenericSateMachineController.X_PROCESS_RESULT, jsonResult);
        }
        //Mandiamo l'evento inizale...
        Message<String> msg = MessageBuilder
                .withPayload(actionResult.evento())
                .build();
        machine.sendEvent(Mono.just(msg))
                .subscribe(s->{
                    log.atInfo().setMessage("{} ...evento {} inviato")
                            .addArgument(uuid)
                            .addArgument(actionResult.evento())
                            .log();
                });
    }

    public abstract ActionResult<T,R> execute(String uuid, T businessCtx, BusinessState bs, StateContext<String, String> stateContext);


}
