package org.gds.poc.orch.ssm.libreria;

import io.netty.handler.logging.LogLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

public abstract class GenericStateMachineAction<T> implements Action<String, String> {
    private final Logger log = LoggerFactory.getLogger(GenericStateMachineAction.class);

    private final WebClient webClient;
    private final Class<T> businessCtxClass;

    public GenericStateMachineAction(WebClient.Builder webClientBuilder,String endpoint, Class<T> businessCtxClass) {
        HttpClient httpClient = HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);

        this.webClient = webClientBuilder.baseUrl(endpoint)
        .clientConnector(new ReactorClientHttpConnector(httpClient))
    .build();
        log.atInfo().setMessage("Endpoint servizi notifica: {}").addArgument(endpoint).log();
        this.businessCtxClass = businessCtxClass;
    }


    @Override
    public final void execute(StateContext<String, String> stateContext) {
        String uuid = (String) stateContext.getMessageHeader(PersistInMemoryHandler.X_UUID_GENERIC);
        String jsonBusinessContext = (String) stateContext.getMessageHeader(PersistInMemoryHandler.X_BUSINESS_CTX);
        BusinessStatus bs = (BusinessStatus) stateContext.getMessageHeader(PersistInMemoryHandler.X_BUSINESS_STATUS);
        T businessCtx = null;
        if(jsonBusinessContext!=null){
            businessCtx = BusinessContextJsonSerializer.deserialize(jsonBusinessContext,businessCtxClass);
        }
        execute(uuid, businessCtx, bs, stateContext);
    }

    public abstract void execute(String uuid, T businessCtx, BusinessStatus bs, StateContext<String, String> stateContext);

    protected void notificaEvento(String uuid, String evento, T businessContext, BusinessStatus nuovoBusinessStatus) {
        log.atInfo().setMessage("Richiesa notifica evento  uuid: {}, evento: {}, business Ctx: {}, businessStatus: {}")
                .addArgument(uuid).addArgument(evento).addArgument(businessContext).addArgument(nuovoBusinessStatus).log();
        String jsonBusinessCtx = null;
        if(businessContext!=null){
            jsonBusinessCtx = BusinessContextJsonSerializer.serialize(businessContext);
        }
        DTOEvent event =new DTOEvent(uuid,evento, jsonBusinessCtx,nuovoBusinessStatus);
        log.atInfo().setMessage("DTO richiesta generato {}").addArgument(event).log();

        webClient.post().uri("/events")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(event), DTOEvent.class)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();

    }



}
