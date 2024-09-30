package org.gds.poc.orch.ssm.libreria.notifiche;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class OrchestrationManagerNotification extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda:processo-avviato")
                .routeId("processo-avvato")
                .log("Da notificare processo avviato ${body}")
                //Trasformiamo il body in json
                .marshal().json(JsonLibrary.Jackson)
                .to("spring-rabbitmq:orchestrazioni?routingKey=create")
                .end();
        from("seda:stato-cambato")
                .routeId("stato-cambiato")
                .log("Da notificare stato cambiato ${body}")
                //Trasformiamo il body in json
                .marshal().json(JsonLibrary.Jackson)
                .to("spring-rabbitmq:orchestrazioni?routingKey=update")
                .end();

    }
}
