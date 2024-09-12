package org.gds.poc.ssm.order;

public class Order {
    private OrderStatusEnum orderStatus;
    private final Integer id;

    public Order(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setOrderStatus(OrderStatusEnum orderStatus) {
        this.orderStatus = orderStatus;
    }

    public OrderStatusEnum getOrderStatus() {
        return orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderStatus=" + orderStatus +
                ", id=" + id +
                '}';
    }

}
