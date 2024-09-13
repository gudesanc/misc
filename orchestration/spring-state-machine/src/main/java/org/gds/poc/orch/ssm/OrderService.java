package org.gds.poc.orch.ssm;

public interface OrderService {
    Order create();
    Order pay(int id);
    Order deliver(int id);
    Order receive(int id);
}