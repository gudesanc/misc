package org.gds.poc.orch.camel.vebalizzazione;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.saga.InMemorySagaService;
import org.gds.pkg.orch.camel.OrchestrationRouteBuilder;
import org.gds.poc.orch.camel.client.protocollo.DtoCreaProtocollo;
import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
import org.gds.poc.orch.camel.client.verbale.DtoVerbale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class VerbalizzazioneRoute extends OrchestrationRouteBuilder<VerbalizzazioneContext,Void> {

    public static final String HEAD_UUID="HEAD_UUID";
    public static final String OPT_COMPENSATION="OPT_COMP";

    @Autowired
    private CamelContext camelContext;
    @Override
        public void configurazioneOrchestrazione() throws Exception {
        //per la in memory
        camelContext.addService(new InMemorySagaService());

        orchestrazioneFrom("seda:start-verbalizzazione",
                "orchestrazione-verbalizzazione",
                "${header."+HEAD_UUID+"}",
                new VerbalizzazioneContext()
                )
                .timeout(2, TimeUnit.MINUTES)
                    .compensation("direct:verbalizzazione-concluso-fallimento")
                    .completion("direct:verbalizzazione-concluso-successo")
                .to("direct:verbalizzazione-creazione-verbale")
                .to("direct:verbalizzazione-protocolla-verbale")
                .to("direct:verbalizzazione-consolida-verbale")
                .end();

        stepFrom("direct:verbalizzazione-creazione-verbale","verbalizzazione-creazione-verbale")
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option(OPT_COMPENSATION,simple(getCtxAsSimpleExpression()))
                    .compensation("direct:verbalizzazione-annulla-verbale")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .marshal().json(JsonLibrary.Jackson)
                // con {{client.verbale.endpoint}} viene letta la proprietà di application.yaml
                .to("{{client.verbale.endpoint}}/verbali/")
                .unmarshal().json(JsonLibrary.Jackson, DtoVerbale.class)
                .log(SIMPLE_EX_UUID+" - Verbale creato- ${body}")
                .process(exchange -> {
                    DtoVerbale verbale = exchange.getMessage().getBody(DtoVerbale.class);

                    VerbalizzazioneContext ctx = getContext(exchange);
                    ctx.setIdVerbale(verbale.id());
                    ctx.setOggettoVerbale(verbale.oggetto());
                })
                .end();


        stepFrom("direct:verbalizzazione-protocolla-verbale","verbalizzazione-protocolla-verbale")
                .saga()
                    .propagation(SagaPropagation.MANDATORY)
                    .option(OPT_COMPENSATION,simple(getCtxAsSimpleExpression()))
                    .compensation("direct:verbalizzazione-annulla-protocollo")

                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setBody(exchange -> {
                    VerbalizzazioneContext ctx = getContext(exchange);
                    return new DtoCreaProtocollo(ctx.getOggettoVerbale(), "mittent", "destinatario");
                }).marshal().json(JsonLibrary.Jackson)
                .to("{{client.protocollo.endpoint}}/protocolli/")
                .unmarshal().json(JsonLibrary.Jackson, DtoProtocollo.class)
                .process(exchange -> {
                    getContext(exchange).setProtocollo(
                            exchange.getMessage().getBody(DtoProtocollo.class)
                    );
                })
                .log(SIMPLE_EX_UUID+" - Protocollo acquisito- ${body}")
                .end();


        stepFrom("direct:verbalizzazione-consolida-verbale","verbalizzazione-consolida-verbale")
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_PATH, simple("/verbali/" + getCtxPropertyAsSimpleExpression("idVerbale") + "/protocollo"))
                .setBody(exchange -> {
                    VerbalizzazioneContext ctx = getContext(exchange);
                    return ctx.getProtocollo();
                }).marshal().json(JsonLibrary.Jackson)
                .toD("{{client.verbale.endpoint}}")
                .log(SIMPLE_EX_UUID+" - Verbale consolidato - ${body}")
                .end();

        stepFrom("direct:verbalizzazione-annulla-verbale","verbalizzazione-annulla-verbale")
                .choice()
                .when(simple(getCtxPropertyAsSimpleExpression("idVerbale") +"  != null"))
                    .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setHeader(Exchange.HTTP_PATH, simple("/verbali/"+getCtxPropertyAsSimpleExpression("idVerbale") +"/annulla"))
                    .setBody(simple(null))
                    .toD("{{client.verbale.endpoint}}")
                    .log(SIMPLE_EX_UUID+" - Verbale annullato ("+getCtxPropertyAsSimpleExpression("idVerbale")+")")
                .otherwise()
                    .log(SIMPLE_EX_UUID+" -  NON NECESSARIO ANNULLARE IL VERBALE ("+getCtxPropertyAsSimpleExpression("idVerbale")+")")
                .end()
        ;

        stepFrom("direct:verbalizzazione-annulla-protocollo","verbalizzazione-annulla-protocollo")
                .choice()
                .when(simple(getCtxPropertyAsSimpleExpression("protocollo")+" != null"))
                    .setHeader(Exchange.HTTP_METHOD, constant("DELETE"))
                    .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                    .setHeader(Exchange.HTTP_PATH, simple("/protocolli/"+getCtxPropertyAsSimpleExpression("protocollo.struttura")+"/"+getCtxPropertyAsSimpleExpression("protocollo.anno")+"/"+getCtxPropertyAsSimpleExpression("protocollo.progressivo")))
                    .setBody(simple(null))
                    .log("BODY: ${body}")
                    .toD("{{client.protocollo.endpoint}}")
                    .log(SIMPLE_EX_UUID+"  - Protcollo annullato ("+getCtxPropertyAsSimpleExpression("protocollo")+")")
                .otherwise()
                    .log(SIMPLE_EX_UUID+"  - NON NECESSARIO ANNULLARE IL PROTOCOLLO ("+getCtxPropertyAsSimpleExpression("protocollo")+")")
                .end()
        ;


//        stepFrom("direct:verbalizzazione-avvio","db-job-verbalizzazione-start")
////                .log(SIMPLE_EX_UUID+" - SALVARE SUL DB L'AVVIO DEL PROCESSO")
//                .log("fff")
//                .end();
        stepFrom("direct:verbalizzazione-concluso-successo","db-job-verbalizzazione-ok")
                .log(SIMPLE_EX_UUID+" - SALVARE SUL DB L'ESITO POSITIVO DEL PROCESSO")
                .end();
        stepFrom("direct:verbalizzazione-concluso-fallimento","db-job-verbalizzazione-ko")
                .log(SIMPLE_EX_UUID+" - SALVARE SUL DB L'ESITO NEGATIVO DEL PROCESSO")
                .end();
    }
}
