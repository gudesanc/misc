package org.gds.poc.ssm.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;

@Configuration
public class PersistHandlerConfig {
    @Autowired
    private StateMachine<String, String> stateMachine;

    @Bean
    public PersistInMemoryHandler persist() {
        return new PersistInMemoryHandler(persistStateMachineHandler());
    }

    @Bean
    public PersistStateMachineHandler persistStateMachineHandler() {
        return new PersistStateMachineHandler(stateMachine);
    }
}
