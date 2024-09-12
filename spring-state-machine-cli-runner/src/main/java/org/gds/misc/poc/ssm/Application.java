package org.gds.misc.poc.ssm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.statemachine.StateMachine;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private StateMachine<StateMachineConfig.States, StateMachineConfig.Events> stateMachine;

    @Override
    public void run(String... args) throws Exception {
        stateMachine.sendEvent(StateMachineConfig.Events.E1);
        stateMachine.sendEvent(StateMachineConfig.Events.E2);
    }
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}