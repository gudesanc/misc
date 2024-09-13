package org.gds.poc.orch.ssm;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Resource
    private OrderService orderService;
    @GetMapping("/order")
    public String createOrder(){
        Order o =orderService.create();
        System.out.println("Order：" + o);
        return getResponse(o);
    }

    @GetMapping("/order/{id}/payed")
    public String payOrder(@PathVariable int id){
        Order current = orderService.pay(id);
        System.out.println("current：" + current);
        return getResponse(current);
    }
    @GetMapping("/order/{id}/delivered")
    public String deliverOrder(@PathVariable int id){
        Order current = orderService.deliver(id);
        System.out.println("current：" + current);
        return getResponse(current);
    }
    @GetMapping("/order/{id}/received")
    public String recivedOrder(@PathVariable int id){
        Order current = orderService.receive(id);
        System.out.println("current：" + current);
        return getResponse(current);
    }

    private String getResponse(Order o){
        return "{\"id\":"+o.getId()+",\"stato\":\""+o.getOrderStatus()+"\"}";
    }


}