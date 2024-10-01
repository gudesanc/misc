package org.gds.pkg.orch.camel.notification;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;
import org.springframework.stereotype.Component;

@Component
public class OrchestrationRuotePolicy extends RoutePolicySupport {
    @Override
    public void onExchangeBegin(Route route, Exchange exchange) {
        System.out.println(msg("ORCHESTRAZIONE AVVIO: "+route.getRouteId()));
    }

    @Override
    public void onExchangeDone(Route route, Exchange exchange) {
        System.out.println(msg("ORCHESTRAIONE FINE onExchangeDone: "+route.getRouteId()
                +"--- FALLITO? "+exchange.isFailed()
                + " --- Eccezione" + exchange.getException()) );

    }

    private String msg(String what){
        return """
                
                *****************************************
                """
                +what+
                """
                   
                   *******************************************
                   
                   
                   """;
    }
}
