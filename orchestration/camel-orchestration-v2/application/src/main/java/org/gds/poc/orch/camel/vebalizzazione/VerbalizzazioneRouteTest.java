//package org.gds.poc.orch.camel.vebalizzazione;
//
//import org.apache.camel.Exchange;
//import org.apache.camel.builder.RouteBuilder;
//import org.apache.camel.model.dataformat.JsonLibrary;
//import org.gds.poc.orch.camel.client.protocollo.DtoCreaProtocollo;
//import org.gds.poc.orch.camel.client.protocollo.DtoProtocollo;
//import org.gds.poc.orch.camel.client.verbale.DtoVerbale;
//import org.springframework.stereotype.Component;
//
//@Component
//public class VerbalizzazioneRouteTest extends RouteBuilder {
//
//    public static final String X_UUID_OPERAZIONE = "X_UUID_OPERAZIONE";
//    public static final String X_CTX_OPERAZIONE = "X_CTX_OPERAZIONE";
//
//
//    @Override
//    public void configure() throws Exception {
//        from("seda:start-verbalizzazione")
//                .routeId("orchestrazione-verbalizzazione")
////                .onCompletion()
////                    .onFailureOnly()
////                        .log("${header." + X_UUID_OPERAZIONE + "} - Processo concluso con ERRORE")
////                .end()
////                .saga()
//                .log("${header." + X_UUID_OPERAZIONE + "} - Avvio processo- ${body}")
//                .process(exchange -> {
//                    exchange.getMessage().setHeader(X_CTX_OPERAZIONE, new VerbalizzazioneContext());
//                })
//                .to("direct:verbalizzazione-creazione-verbale")
//                //metiamo nel body quello che serve per protocollar
//                .setBody(exchange -> {
//                    VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE);
//                    return new DtoCreaProtocollo(ctx.getOggettoVerbale(),"mittent","destinatario");
//                })
////                .circuitBreaker()
//                    .to("direct:verbalizzazione-protocolla-verbale")
////                .onFallback()
////                    .log("Circuito aperto *****************")
////                    .to("direct:verbalizzazione-annulla-verbale")
////                //Occhio che poi continua...
////                .end()
//                //ok nel body c'e' gia' il protocollo
//                .to("direct:verbalizzazione-consolida-verbale")
//                .log("${header." + X_UUID_OPERAZIONE + "} - Processo concluso con successo")
//                .end();
//
//        from("direct:verbalizzazione-creazione-verbale").
//                routeId("verbalizzazione-creazione-verbale")
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//                .marshal().json(JsonLibrary.Jackson)
//                // con {{client.verbale.endpoint}} viene letta la proprietà di application.yaml
//                .to(/*endpointVerbale + */"{{client.verbale.endpoint}}/verbali/")
//                .unmarshal().json(JsonLibrary.Jackson, DtoVerbale.class)
//                .log("${header."+X_UUID_OPERAZIONE+"} - Verbale creato- ${body}")
//                .process(exchange -> {
//                    DtoVerbale verbale =exchange.getMessage().getBody(DtoVerbale.class) ;
//                    VerbalizzazioneContext ctx = ((VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE));
//                    ctx.setIdVerbale(verbale.id());
//                    ctx.setOggettoVerbale(verbale.oggetto());
//                })
//                .end();
//
//
//        from("direct:verbalizzazione-protocolla-verbale")
//                .routeId("verbalizzazione-protocolla-verbale")
////                .saga()
////                .propagation(SagaPropagation.MANDATORY)
////                .option(X_CTX_OPERAZIONE,exchangeProperty(X_CTX_OPERAZIONE))
////                .compensation("direct:verbalizzazione-annulla-verbale")
//                //invochamo il servizio rest di protocollazione
//                //In teoria dovrebbe gestirla la saga
////                .onException(HttpOperationFailedException.class)
////                    .log("Acc ${body} ora bisognerebbe" )
//////                    .to("direct:verbalizzazione-annulla-verbale")
////                    .handled(true)
////                .end()
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
////                .setBody(exchange -> {
////                    VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getProperty(X_CTX_OPERAZIONE);
////                    return new DtoCreaProtocollo(ctx.getOggettoVerbale(), "mittente", "destinatario");
////                })
//                .marshal().json(JsonLibrary.Jackson)
//                .to("{{client.protocollo.endpoint}}/protocolli/")
//                .unmarshal().json(JsonLibrary.Jackson,DtoProtocollo.class)
//                .process(exchange -> {
//                    ((VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE)).setProtocollo(
//                            exchange.getMessage().getBody(DtoProtocollo.class)
//                    );
//                })
//                .log("${header."+X_UUID_OPERAZIONE+"} - Protocollo acquisito- ${body}")
//                .end();
////                .log("Protocollo Acquisito: ${body}")
////                .to("direct:verbalizzazione-consolidamento");
//
//    from("direct:verbalizzazione-consolida-verbale")
//            .routeId("verbalizzazione-consolida-verbale")
//            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//            .setHeader(Exchange.HTTP_PATH,simple("/verbali/${header."+X_CTX_OPERAZIONE+".idVerbale}/protocollo"))
//            .setBody(exchange -> {
//                VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE);
//                return ctx.getProtocollo();
//            }).marshal().json(JsonLibrary.Jackson)
//            .toD("{{client.verbale.endpoint}}" )
//            .log("${header."+X_UUID_OPERAZIONE+"} - Verbale consolidato - ${body}")
//            .end();
//
//    from("direct:verbalizzazione-annulla-verbale")
//            .routeId("verbalizzazione-annulla-verbale")
//            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//            .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//            .setHeader(Exchange.HTTP_PATH,simple("/verbali/${header."+X_CTX_OPERAZIONE+".idVerbale}/annulla"))
//            .setBody(exchange -> {
//                VerbalizzazioneContext ctx = (VerbalizzazioneContext) exchange.getMessage().getHeader(X_CTX_OPERAZIONE);
//                return ctx.getProtocollo();
//            }).marshal().json(JsonLibrary.Jackson)
//            .toD("{{client.verbale.endpoint}}" )
//            .log("Verbale "+header(X_CTX_OPERAZIONE)+" annullato");
//
//    }
//
//
//}
