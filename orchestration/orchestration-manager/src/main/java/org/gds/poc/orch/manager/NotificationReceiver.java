package org.gds.poc.orch.manager;

import jakarta.annotation.Resource;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.gds.poc.orch.manager.library.CreateOrchProcessRequest;
import org.gds.poc.orch.manager.library.UpdateOrchProcessRequest;
import org.springframework.stereotype.Component;

@Component
public class NotificationReceiver extends RouteBuilder {
    @Resource
    private final OrchestrationProcessService orchestrationProcessService;

    public NotificationReceiver(OrchestrationProcessService orchestrationProcessService) {
        this.orchestrationProcessService = orchestrationProcessService;
    }

    @Override
    public void configure() throws Exception {
        from("spring-rabbitmq:orchestrazioni?routingKey=create&acknowledgeMode=AUTO")
                .routeId("notifica-processo-avvato")
                .log("Ricevuta notifica processo avviato ${body}")
                .unmarshal().json(JsonLibrary.Jackson, CreateOrchProcessRequest.class)
                .bean(orchestrationProcessService,"createNewProcess")
                .end();
        from("spring-rabbitmq:orchestrazioni?routingKey=update&acknowledgeMode=AUTO")
                .routeId("nofitica-stato-cambiato")
                .log("Ricevuta notifica stato cambiato ${body}")
                .unmarshal().json(JsonLibrary.Jackson, UpdateOrchProcessRequest.class)
                .bean(orchestrationProcessService,"aggiornaStatoProcesso")
                .end();
    }
}
