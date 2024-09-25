package org.gds.poc.orch.ssm.libreria;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;


@Component
public class StateMachineLogListener extends StateMachineListenerAdapter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(StateMachineLogListener.class);

    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        StringBuilder sb = new StringBuilder();
        if (stateContext.getStage() == Stage.STATE_ENTRY) {
            sb.append(stateContext.getStateMachine().getId()).append(" enter ").append(stateContext.getTarget().getId());
        } else if (stateContext.getStage() == Stage.STATE_EXIT) {
            sb.append(stateContext.getStateMachine().getId()).append(" exit ").append(stateContext.getSource().getId());
        }
        log.atInfo().setMessage("===================\n{}\n=======================").addArgument(sb).log();
    }
}