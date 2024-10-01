package org.gds.pkg.orch.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.NamedNode;
import org.apache.camel.spi.RoutePolicy;
import org.gds.pkg.orch.camel.notification.OrchestrationRuotePolicy;

public class OrchestrationRuotePolicyFactory implements org.apache.camel.spi.RoutePolicyFactory{
    @Override
    public RoutePolicy createRoutePolicy(CamelContext camelContext, String routeId, NamedNode route) {
        return new OrchestrationRuotePolicy();
    }
}
