package org.gds.poc.orch.camel.vebalizzazione;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.gds.poc.orch.camel.client.protocollo.DtoCreaProtocollo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class VerbalizzazioneRoute extends RouteBuilder {

    public static final String X_CTX_OPERAZIONE = "X-CTX-OPERAZIONE";
    @Value("${client.verbale.endpoint}")
    private String endpointVerbale;
    @Value("${client.protocollo.endpoint}")
    private String endpointProtocollazione;

    @Override
    public void configure() throws Exception {
        from("direct:start-verbalizzazione")
                .routeId("orchestrazione-verbalizzazione")
//                .saga()
                .log("DAJE:  ${header.X-UUID-OPERAZIONE} - ${body}")
                .process(exchange -> {
                    exchange.setProperty(X_CTX_OPERAZIONE,exchange.getMessage().getBody());
                })
                .to("direct:verbalizzazione-protocollazione")
                .end();

        from("direct:verbalizzazione-protocollazione")
                .routeId("verbalizzazione-acquisizione-protocollo")
//                .saga()
//                .propagation(SagaPropagation.MANDATORY)
//                .compensation("direct:verbalizzazione-annulla-verbale")
                //invochamo il servizio rest di protocollazione
                //In teoria dovrebbe gestirla la saga
                .onException(HttpOperationFailedException.class)
                .log("Acc ${body}")
                .handled(true)
                .end()
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(exchange -> {
                    VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getProperty(X_CTX_OPERAZIONE);
                    return new DtoCreaProtocollo(ctx.getOggettoVerbale(), "mittente", "destinatario");
                }).marshal().json(JsonLibrary.Jackson)
                .to(endpointProtocollazione + "/protocolli/")
                .log("DAJE:  ${header.X-UUID-OPERAZIONE} - ${body}");
    }
}
