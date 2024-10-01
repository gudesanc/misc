package org.gds.pkg.orch.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.gds.pkg.orch.camel.notification.OrchestrationRuotePolicy;
import org.gds.pkg.orch.camel.notification.StepRuotePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class OrchestrationRouteBuilder extends RouteBuilder {
    @Autowired
    private CamelContext camelContext;
    @Autowired
    private OrchestrationRuotePolicy orchestrationRuotePolicy;
    @Autowired
    private StepRuotePolicy stepRuotePolicy;


    protected RouteDefinition orchestrazioneFrom(
            String uri,
            String nomeOrchestrazione
            ){
        return from(uri)
                .routePolicy(orchestrationRuotePolicy)
                .routeId(nomeOrchestrazione);
    }

    protected RouteDefinition stepFrom(String uri, String nomeStep){
        return from(uri)
                .routePolicy(stepRuotePolicy)
                .routeId(nomeStep);

    }
}
