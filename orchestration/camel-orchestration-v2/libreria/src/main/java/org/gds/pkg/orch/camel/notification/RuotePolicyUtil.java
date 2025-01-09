package org.gds.pkg.orch.camel.notification;

import org.apache.camel.Exchange;
import org.gds.pkg.orch.camel.OrchestrationRouteBuilder;

public class RuotePolicyUtil {
    public static String msg(String what, Exchange exchange){
        return """
                
                *****************************************
                """
                +what+ " "+ getIds(exchange)+
                """
                   
                   *******************************************
                   
                   
                   """;
    }

    private static String getIds(Exchange e){
        return "[UUID: " +
                e.getProperty(OrchestrationRouteBuilder.X_UUID_OPERAZIONE) +
                ", LRA: " +
                e.getIn().getHeader(Exchange.SAGA_LONG_RUNNING_ACTION) +
                ", ctx: " +
                e.getProperty(OrchestrationRouteBuilder.X_CTX_OPERAZIONE) +
                ", Result: " +
                e.getProperty(OrchestrationRouteBuilder.X_RISULTATO_OPERAZIONE)
                ;
    }
}
