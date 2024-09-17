package org.gds.poc.orch.ssm.client.verbale;

import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class VerbaleServiceImpl implements VerbaleService {
    private final Logger log = LoggerFactory.getLogger(VerbaleServiceImpl.class);
    private final WebClient webClient;

    public VerbaleServiceImpl(WebClient.Builder webClientBuilder, @Value("${client.verbale.endpoint}") String endpoint) {
        this.webClient = webClientBuilder.baseUrl(endpoint).build();
        log.atInfo().setMessage("Endpoint servizi protocollo: {}").addArgument(endpoint).log();
    }

    @Override
    public Mono<DtoVerbale> creaVerbale(DtoCreaVerbale infoCreazione) {
        log.atDebug().setMessage("Richiesta creazione verbale per: {}").addArgument(infoCreazione).log();
        return webClient
                .post()
                .uri("/verbali/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(infoCreazione), DtoCreaVerbale.class)
                .retrieve()
                .bodyToMono(DtoVerbale.class)
                .doOnSuccess(response -> {
                    // Log the successful response
                    log.atInfo().setMessage("Creazione verbale per {} effettuata con successo: {}")
                            .addArgument(infoCreazione)
                            .addArgument(response)
                            .log();
                })
                .doOnError(error -> {
                    log.atWarn().setMessage("Creazioen verbale per: {} fallita, errore: {}")
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

    @Override
    public Mono<DtoVerbale> consolidaVerbale(String idVerbale, DtoProtocollo protocollo) {
        log.atDebug().setMessage("Richiesta consolidamento verbale {} con protocollo {}")
                .addArgument(idVerbale)
                .addArgument(protocollo)
                .log();
        return webClient
                .post()
                .uri("/verbali/{idVerbale}/protocollo",idVerbale)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(protocollo), DtoProtocollo.class)
                .retrieve()
                .bodyToMono(DtoVerbale.class)
                .doOnSuccess(response -> {
                    // Log the successful response
                    log.atInfo().setMessage("Consolidamento verbale {}  con {} effettuata con successo: {}")
                            .addArgument(idVerbale)
                            .addArgument(protocollo)
                            .addArgument(response)
                            .log();
                })
                .doOnError(error -> {
                    log.atWarn().setMessage("Consolidamento verbale {} fallito, errore: {}")
                            .addArgument(idVerbale)
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

    @Override
    public Mono<DtoVerbale> annullaVerbale(String idVerbale) {
        log.atDebug().setMessage("Richiesta annullamento verbale {} ")
                .addArgument(idVerbale)
                .log();
        return webClient
                .post()
                .uri("/verbali/{idVerbale}/annulla",idVerbale)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(DtoVerbale.class)
                .doOnSuccess(response -> {
                    // Log the successful response
                    log.atInfo().setMessage("Annullamento verbale {}  effettuata con successo: {}")
                            .addArgument(idVerbale)
                            .addArgument(response)
                            .log();
                })
                .doOnError(error -> {
                    log.atWarn().setMessage("Annullamento verbale {} fallito, errore: {}")
                            .addArgument(idVerbale)
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
