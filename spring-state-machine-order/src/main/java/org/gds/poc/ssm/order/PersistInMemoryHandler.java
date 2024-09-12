package org.gds.poc.ssm.order;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler;
import org.springframework.statemachine.recipes.persist.PersistStateMachineHandler.PersistStateChangeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.HashMap;
import java.util.Map;

public class PersistInMemoryHandler {
    private final PersistStateMachineHandler handler;
    private final static Map<Integer,Order> ordini = new HashMap<>();
    private final PersistStateChangeListener listener = new LocalPersistStateChangeListener();


    public PersistInMemoryHandler(PersistStateMachineHandler persistStateMachineHandler) {
        this.handler=persistStateMachineHandler;
        this.handler.addPersistStateChangeListener(listener);
    }

    public Order addNewOrder(){
        Integer id =ordini.keySet().stream().reduce(Integer::max).orElse(0)+1;
        Order order = new Order(id);
        order.setOrderStatus(OrderStatusEnum.WAIT_PAYMENT);
        ordini.put(id,order);
        return order;
    }

    public Order change(int order, String event) {
        Order o = ordini.get(order);
        handler.handleEventWithStateReactively(MessageBuilder
                        .withPayload(event).setHeader("idOrder", order).build(), o.getOrderStatus().name())
                .block();
        //Metto block solo per vedere se effetivamente cambia stato
        return o;
    }


    private class LocalPersistStateChangeListener implements PersistStateMachineHandler.PersistStateChangeListener {

        @Override
        public void onPersist(State<String, String> state, Message<String> message,
                              Transition<String, String> transition, StateMachine<String, String> stateMachine) {
            if (message != null && message.getHeaders().containsKey("idOrder")) {
                Integer idOrder = message.getHeaders().get("idOrder", Integer.class);
                ordini.get(idOrder).setOrderStatus(OrderStatusEnum.valueOf(state.getId()));
            }
            System.out.println(ordini);
        }
    }
}
