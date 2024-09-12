package org.gds.poc.ssm.order;

public interface OrderService {
    Order create();
    Order pay(int id);
    Order deliver(int id);
    Order receive(int id);
}