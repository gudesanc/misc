package org.gds.poc.orch.ssm.libreria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;
import org.springframework.stereotype.Component;


@Component
public class StateMachineLogListener extends StateMachineListenerAdapter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineLogListener.class);

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        if (stateContext.getStage() == Stage.STATE_ENTRY) {
            log.atInfo().setMessage("Entring: {}")
                    .addArgument(getInfoStato(stateContext.getTarget()))
                    .log();
        } else if (stateContext.getStage() == Stage.STATE_EXIT) {
            log.atInfo().setMessage("Exiting: {}")
                    .addArgument(getInfoStato(stateContext.getSource()))
                    .log();
        } else if (stateContext.getStage() == Stage.STATE_CHANGED) {
            log.atInfo().setMessage("Changing from {} to {}")
                    .addArgument(getInfoStato(stateContext.getSource()))
                    .addArgument(getInfoStato(stateContext.getTarget()))
                    .log();
        }
    }

    private String getInfoStato(State<String,String> state){
        if(state==null){
            return "-";
        }
        return state.getId();
    }
}