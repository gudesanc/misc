package org.gds.poc.ssm.order;

import jakarta.annotation.Resource;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    PersistInMemoryHandler persistInMemoryHandler;
    @Override
    public Order create() {
        Order order = persistInMemoryHandler.addNewOrder();
        System.out.println("order create success:" + order.toString());
        return order;
    }
    @Override
    public Order pay(int id) {
        return persistInMemoryHandler.change(id,OrderStatusChangeEventEnum.PAYED.name());
    }
    @Override
    public Order deliver(int id) {
        return persistInMemoryHandler.change(id,OrderStatusChangeEventEnum.DELIVERY.name());
    }
    @Override
    public Order receive(int id) {
        return persistInMemoryHandler.change(id,OrderStatusChangeEventEnum.RECEIVED.name());
    }

}