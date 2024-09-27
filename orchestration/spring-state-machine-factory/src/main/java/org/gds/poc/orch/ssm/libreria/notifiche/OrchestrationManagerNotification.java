package org.gds.poc.orch.ssm.libreria.notifiche;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrchestrationManagerNotification extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda:processo-avviato")
                .routeId("processo-avvato")
                .log("Da notificare processo avviato ${body}")
                .end();
        from("seda:stato-cambato")
                .routeId("stato-cambiato")
                .log("Da notificare stato cambiato ${body}");

    }
}
