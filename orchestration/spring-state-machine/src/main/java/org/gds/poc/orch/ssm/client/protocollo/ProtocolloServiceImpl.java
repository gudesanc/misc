package org.gds.poc.orch.ssm.client.protocollo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class ProtocolloServiceImpl implements ProtocolloService {
    private final Logger log = LoggerFactory.getLogger(ProtocolloServiceImpl.class);
    private final WebClient webClient;

    public ProtocolloServiceImpl(WebClient.Builder webClientBuilder, @Value("${client.protocollo.endpoint}") String endpoint) {
        this.webClient = webClientBuilder.baseUrl(endpoint).build();
        log.atInfo().setMessage("Endpoint servizi protocollo: {}").addArgument(endpoint).log();
    }

    public Mono<DtoProtocollo> protocolla(DtoCreaProtocollo infoCreazione){
        log.atDebug().setMessage("Richiesta protocollazione per: {}").addArgument(infoCreazione).log();
        return webClient
                .post()
                .uri("/protocolli/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(infoCreazione),DtoCreaProtocollo.class)
                .retrieve()
                .bodyToMono(DtoProtocollo.class)
                .doOnSuccess(response -> {
                    // Log the successful response
                    log.atInfo().setMessage("Procollazione per {} effettuata con successo: {}")
                            .addArgument(infoCreazione)
                            .addArgument(response)
                            .log();
                })
                .doOnError(error -> {
                    log.atWarn().setMessage("Procollazione per: {} fallita, errore: {}")
                            .addArgument(infoCreazione)
                            .addArgument(error)
                            .log();
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() != HttpStatus.OK) {
                        return Mono.error(new UnsupportedOperationException("Error: " + ex.getResponseBodyAsString()));
                    }
                    return Mono.error(ex);
                });
    }
    public Mono<Void> annullaProtocollo(DtoProtocollo protocollo){
        log.atDebug().setMessage("Richiesta anullamento per: {}").addArgument(protocollo).log();
        return webClient
                .method(HttpMethod.DELETE)
                .uri("/protocolli/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(protocollo),DtoProtocollo.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response -> {
                    // Log the successful response
                    log.atInfo().setMessage("Procollo {} annullato con successo")
                            .addArgument(protocollo)
                            .log();
                })
                .doOnError(error -> {
                    log.atWarn().setMessage("Errore annullamento protocollo {}, errore: {}")
                            .addArgument(protocollo)
                            .addArgument(error)
                            .log();
                })
                .onErrorResume(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() != HttpStatus.OK) {
                        return Mono.error(new UnsupportedOperationException("Error: " + ex.getResponseBodyAsString()));
                    }
                    return Mono.error(ex);
                });

    }

}
