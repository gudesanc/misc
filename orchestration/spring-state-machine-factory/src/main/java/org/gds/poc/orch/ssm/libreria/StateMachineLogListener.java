package org.gds.poc.orch.ssm.libreria;

import jakarta.annotation.Resource;
import org.apache.camel.ProducerTemplate;
import org.gds.poc.orch.ssm.libreria.notifiche.UpdateOrchProcessRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


@Component
public class StateMachineLogListener extends StateMachineListenerAdapter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineLogListener.class);
    @Resource
    private ProducerTemplate producerTemplate;
    @Override
    public void stateContext(StateContext<String, String> stateContext) {




        if (stateContext.getStage() == Stage.STATE_ENTRY) {
            String uuid = getStringExtendedVariable(stateContext,GenericSateMachineController.X_UUID_GENERIC);
            BusinessState businessState = (BusinessState) stateContext.getExtendedState().getVariables().get(GenericSateMachineController.X_BUSINESS_STATUS);
            String nuovoStato = getInfoStato(stateContext.getTarget());
            String ctx = getStringExtendedVariable(stateContext,GenericSateMachineController.X_BUSINESS_CTX);
            String result = getStringExtendedVariable(stateContext,GenericSateMachineController.X_PROCESS_RESULT);
            log.atInfo().setMessage("Entring: {} - UUID: {} - ctx {} - BS {} - Risultato {}")
                    .addArgument(nuovoStato)
                    .addArgument(uuid)
                    .addArgument(ctx)
                    .addArgument(businessState)
                    .addArgument(result)
                    .log();
            UpdateOrchProcessRequest updateOrchProcessRequest = new UpdateOrchProcessRequest(
                    uuid,LocalDateTime.now(),nuovoStato,businessState,ctx,result
            );
            producerTemplate.sendBody("seda:stato-cambato",updateOrchProcessRequest );
//        } else if (stateContext.getStage() == Stage.STATE_EXIT) {
//            log.atInfo().setMessage("Exiting: {}")
//                    .addArgument(getInfoStato(stateContext.getSource()))
//                    .log();
//        } else if (stateContext.getStage() == Stage.STATE_CHANGED) {
//            log.atInfo().setMessage("Changing from {} to {}")
//                    .addArgument(getInfoStato(stateContext.getSource()))
//                    .addArgument(getInfoStato(stateContext.getTarget()))
//                    .log();
        }
    }

    private String getInfoStato(State<String,String> state){
        if(state==null){
            return "-";
        }
        return state.getId();
    }

    private String getStringExtendedVariable(StateContext<String, String> stateContext, String variableName){
        return (String) stateContext.getExtendedState().getVariables().get(variableName);
    }
}