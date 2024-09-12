package org.gds.poc.ssm.order;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableStateMachine
public class OrderStatusMachineConfig extends StateMachineConfigurerAdapter<String, String> {
    /**
     * configure state
     */
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(OrderStatusEnum.WAIT_PAYMENT.name())
                .end(OrderStatusEnum.FINISH.name())
                .states(Arrays.stream(OrderStatusEnum.values()).map(OrderStatusEnum::name).collect(Collectors.toSet()));
    }

    /**
     * configure state transient  with event
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatusEnum.WAIT_PAYMENT.name()).target(OrderStatusEnum.WAIT_DELIVER.name())
                .event(OrderStatusChangeEventEnum.PAYED.name())
                .and().withExternal()
                .source(OrderStatusEnum.WAIT_DELIVER.name()).target(OrderStatusEnum.WAIT_RECEIVE.name()).event(OrderStatusChangeEventEnum.DELIVERY.name())
                .and().withExternal()
                .source(OrderStatusEnum.WAIT_RECEIVE.name()).target(OrderStatusEnum.FINISH.name()).event(OrderStatusChangeEventEnum.RECEIVED.name());
    }


}