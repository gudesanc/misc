package org.gds.poc.orch.camel.vebalizzazione;

import jakarta.annotation.Resource;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.saga.InMemorySagaService;
import org.gds.poc.orch.camel.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.camel.client.verbale.DtoVerbale;
import org.springframework.stereotype.Component;

@Component
public class VerbalizzazioneRoute extends RouteBuilder {

    public static final String X_UUID_OPERAZIONE = "X_UUID_OPERAZIONE";
    public static final String X_CTX_OPERAZIONE = "X_CTX_OPERAZIONE";


    @Resource
    private CamelContext camelContext;
    @Override
    public void configure() throws Exception {
        //per la in memory
        camelContext.addService(new InMemorySagaService());

        from("seda:start-verbalizzazione")
                .routeId("orchestrazione-verbalizzazione")
                .saga()
                    .option(X_UUID_OPERAZIONE,simple("${header."+X_UUID_OPERAZIONE+"}"))
                    .compensation("direct:verbalizzazione-concluso-fallimento")
                    .completion("direct:verbalizzazione-concluso-successo")
                .to("direct:verbalizzazione-avvio")
                .process(exchange -> {
                    String uuid = (String) exchange.getMessage().getHeader(X_UUID_OPERAZIONE);
                    exchange.getMessage().setHeader(X_CTX_OPERAZIONE, new VerbalizzazioneContext(uuid));
                })
                .to("direct:verbalizzazione-creazione-verbale")
                .to("direct:verbalizzazione-protocolla-verbale")
                .to("direct:verbalizzazione-consolida-verbale")
                .end();

        from("direct:verbalizzazione-creazione-verbale").
                routeId("verbalizzazione-creazione-verbale")
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option(X_CTX_OPERAZIONE,simple("${header."+X_CTX_OPERAZIONE+"}"))
                    .compensation("direct:verbalizzazione-annulla-verbale")

                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .marshal().json(JsonLibrary.Jackson)
                // con {{client.verbale.endpoint}} viene letta la proprietà di application.yaml
                .to("{{client.verbale.endpoint}}/verbali/")
                .unmarshal().json(JsonLibrary.Jackson, DtoVerbale.class)
                .log("${header." + X_UUID_OPERAZIONE + "} - Verbale creato- ${body}")
                .process(exchange -> {
                    DtoVerbale verbale = exchange.getMessage().getBody(DtoVerbale.class);
                    VerbalizzazioneContext ctx = ((VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE));
                    ctx.setIdVerbale(verbale.id());
                    ctx.setOggettoVerbale(verbale.oggetto());
                })
                .end();


        from("direct:verbalizzazione-protocolla-verbale")
                .routeId("verbalizzazione-protocolla-verbale")
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option(X_CTX_OPERAZIONE,simple("${header."+X_CTX_OPERAZIONE+"}"))
                    .compensation("direct:verbalizzazione-annulla-protocollo")

                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(exchange -> {
                    VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE);
                    return new DtoCreaProtocollo(ctx.getOggettoVerbale(), "mittent", "destinatario");
                }).marshal().json(JsonLibrary.Jackson)
                .to("{{client.protocollo.endpoint}}/protocolli/")
                .unmarshal().json(JsonLibrary.Jackson, DtoProtocollo.class)
                .process(exchange -> {
                    ((VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE)).setProtocollo(
                            exchange.getMessage().getBody(DtoProtocollo.class)
                    );
                })
                .log("${header." + X_UUID_OPERAZIONE + "} - Protocollo acquisito- ${body}")
                .end();


        from("direct:verbalizzazione-consolida-verbale")
                .routeId("verbalizzazione-consolida-verbale")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_PATH, simple("/verbali/${header." + X_CTX_OPERAZIONE + ".idVerbale}/protocollo"))
                .setBody(exchange -> {
                    VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE);
                    return ctx.getProtocollo();
                }).marshal().json(JsonLibrary.Jackson)
                .toD("{{client.verbale.endpoint}}")
                .log("${header." + X_UUID_OPERAZIONE + "} - Verbale consolidato - ${body}")
                .end();

        from("direct:verbalizzazione-annulla-verbale")
                .routeId("verbalizzazione-annulla-verbale")
                .choice()
                .when(simple("${header." + X_CTX_OPERAZIONE + ".idVerbale}  != null"))
                    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setHeader(Exchange.HTTP_PATH, simple("/verbali/${header." + X_CTX_OPERAZIONE + ".idVerbale}/annulla"))
                    .setBody(simple(null))
                    .toD("{{client.verbale.endpoint}}")
                    .log("${header." + X_UUID_OPERAZIONE + "} - Verbale annullato (${header." + X_CTX_OPERAZIONE + ".idVerbale})")
                .otherwise()
                    .log("${header." + X_UUID_OPERAZIONE + "} -  NON NECESSARIO ANNULLARE IL VERBALE (${header." + X_CTX_OPERAZIONE + ".idVerbale})")
                .end()
        ;

        from("direct:verbalizzazione-annulla-protocollo")
                .routeId("verbalizzazione-annulla-protocollo")
                .choice()
                .when(simple("${header." + X_CTX_OPERAZIONE + ".protocollo}  != null"))
                    .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setHeader(Exchange.HTTP_PATH, simple("/protocolli/${header." + X_CTX_OPERAZIONE + ".protocollo.struttura}/${header." + X_CTX_OPERAZIONE + ".protocollo.anno}/${header." + X_CTX_OPERAZIONE + ".protocollo.progressivo}"))
                    .setBody(simple(null))
                    .log("BODY: ${body}")
                    .toD("{{client.protocollo.endpoint}}")
                    .log("${header." + X_UUID_OPERAZIONE + "} - Protcollo annullato (${header."+X_CTX_OPERAZIONE+".protocollo})")
                .otherwise()
                    .log("${header." + X_UUID_OPERAZIONE + "} - NON NECESSARIO ANNULLARE IL PROTOCOLLO (${header."+X_CTX_OPERAZIONE+".protocollo})")
                .end()
        ;


        from("direct:verbalizzazione-avvio")
                .routeId("db-job-verbalizzazione-start")
                .log("${header." + X_UUID_OPERAZIONE + "} - SALVARE SUL DB L'AVVIO DEL PROCESSO")
                .end();
        from("direct:verbalizzazione-concluso-successo")
                .routeId("db-job-verbalizzazione-ok")
                .log("${header." + X_UUID_OPERAZIONE + "} - SALVARE SUL DB L'ESITO POSITIVO DEL PROCESSO")
                .end();
        from("direct:verbalizzazione-concluso-fallimento")
                .routeId("db-job-verbalizzazione-ko")
                .log("${header." + X_UUID_OPERAZIONE + "} - SALVARE SUL DB L'ESITO NEGATIVO DEL PROCESSO")
                .end();
    }
}
