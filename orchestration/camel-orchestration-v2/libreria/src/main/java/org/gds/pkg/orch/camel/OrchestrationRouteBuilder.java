package org.gds.pkg.orch.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.model.SagaDefinition;
import org.gds.pkg.orch.camel.notification.OrchestrationRuotePolicy;
import org.gds.pkg.orch.camel.notification.RuotePolicyUtil;
import org.gds.pkg.orch.camel.notification.StepRuotePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public  abstract class OrchestrationRouteBuilder<C extends OrchestrationContext,R> extends RouteBuilder {
    public static final String X_UUID_OPERAZIONE = "X_UUID_OPERAZIONE";
    public static final String X_CTX_OPERAZIONE = "X_CTX_OPERAZIONE";
    public static final String X_RISULTATO_OPERAZIONE = "X_RISULTATO_OPERAZIONE";

    protected static final String SIMPLE_EX_UUID="${exchangeProperty."+X_UUID_OPERAZIONE+"}";

    @Autowired
    private CamelContext camelContext;
    @Autowired
    private OrchestrationRuotePolicy orchestrationRuotePolicy;
    @Autowired
    private StepRuotePolicy stepRuotePolicy;


    protected SagaDefinition orchestrazioneFrom(
            String uri,
            String nomeOrchestrazione,
            final String uuidSimpleExpression,
            C ctx
            ){

        return from(uri)
                .routePolicy(orchestrationRuotePolicy)
                .routeId(nomeOrchestrazione)
                //Impostiamo UUID
                .setProperty(X_UUID_OPERAZIONE,simple(uuidSimpleExpression))
                //Impostiamo CTX
                .process(x ->{
                    String uuid = x.getProperty(X_UUID_OPERAZIONE,String.class);
                    if(uuid == null){
                        throw new IllegalArgumentException("L'identificativo univoco dell'orchestrazione non può essere null");
                    }
                    ctx.uuid(uuid);
                    x.setProperty(X_CTX_OPERAZIONE,ctx);
                })
                .wireTap("direct:orchestrazione-start")
                .saga()
                //Facciamo in modo che in compensazione il contesto sia presente
//                .option(X_CTX_OPERAZIONE,simple("${exchangeProperty."+X_CTX_OPERAZIONE+"}"))
                ;
    }

    protected RouteDefinition stepFrom(String uri, String nomeStep){
        return from(uri)
                .routePolicy(stepRuotePolicy)
                .routeId(nomeStep);

    }

    protected void addResult(Exchange e, R risultato){
        e.setProperty(X_RISULTATO_OPERAZIONE,risultato);
    }

    protected C getContext(Exchange e){
        return (C)e.getProperty(X_CTX_OPERAZIONE);
    }

    @Override
    public void configure() throws Exception {
        from("direct:orchestrazione-start")
                .process(x -> {
                    System.out.println(RuotePolicyUtil.msg("AVVIO EFFETTIVO DA NOTIFICARE!!!!",x));
                }).end();

        configurazioneOrchestrazione();
    }

    protected abstract void configurazioneOrchestrazione() throws Exception;

    protected String getCtxPropertyAsSimpleExpression(String property){
        StringBuilder result =
                new StringBuilder("${exchangeProperty."+X_CTX_OPERAZIONE);
        if(property!=null){
            if(!property.startsWith(".")){
                result.append(".");
            }
            result.append(property);
        }
        result.append("}");
        return result.toString();
    }
    protected String getCtxAsSimpleExpression(){
        return getCtxPropertyAsSimpleExpression(null);
    }
}
