package org.gds.pkg.orch.camel.notification;

import org.apache.camel.Exchange;
import org.apache.camel.Route;
import org.apache.camel.support.RoutePolicySupport;
import org.gds.pkg.orch.camel.OrchestrationRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrchestrationRuotePolicy extends RoutePolicySupport {
    @Override
    public void onExchangeBegin(Route route, Exchange exchange) {
        System.out.println(RuotePolicyUtil.msg("ORCHESTRAZIONE AVVIO: "+route.getRouteId(),exchange));
    }

    @Override
    public void onExchangeDone(Route route, Exchange exchange) {
        System.out.println(RuotePolicyUtil.msg("ORCHESTRAIONE FINE onExchangeDone: "+route.getRouteId()+
                " --- FALLITO? "+exchange.isFailed()
                + " --- Eccezione" + exchange.getException(),exchange) );

    }


}
