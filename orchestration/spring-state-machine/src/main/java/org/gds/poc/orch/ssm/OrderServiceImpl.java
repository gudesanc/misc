package org.gds.poc.orch.ssm;

import jakarta.annotation.Resource;
import org.gds.poc.orch.ssm.libreria.PersistInMemoryHandler;
import org.springframework.stereotype.Service;

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