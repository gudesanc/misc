package org.gds.poc.orch.ssm.client.verbale;

import org.gds.poc.orch.ssm.client.protocollo.DtoProtocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Service
public class VerbaleServiceImpl implements VerbaleService {
    private final Logger log = LoggerFactory.getLogger(VerbaleServiceImpl.class);
    private final RestClient webClient;

    public VerbaleServiceImpl(RestClient.Builder webClientBuilder, @Value("${client.verbale.endpoint}") String endpoint) {
        this.webClient = webClientBuilder.baseUrl(endpoint).build();
        log.atInfo().setMessage("Endpoint servizi protocollo: {}").addArgument(endpoint).log();
    }

    @Override
    public DtoVerbale creaVerbale(DtoCreaVerbale infoCreazione) {
        log.atDebug().setMessage("Richiesta creazione verbale per: {}").addArgument(infoCreazione).log();

        DtoVerbale verbale = webClient
                .post()
                .uri("/verbali/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(infoCreazione)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    String bodyAsString = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    log.atWarn().setMessage("Creazione verbale per: {} fallita, errore: {} - {}")
                            .addArgument(infoCreazione)
                            .addArgument(response.getStatusCode())
                            .addArgument(bodyAsString)
                            .log();
                    throw new UnsupportedOperationException("Error: " + bodyAsString);

                }))
                .body(DtoVerbale.class);
        log.atInfo().setMessage("Creazione verbale per {} effettuata con successo: {}")
                .addArgument(infoCreazione)
                .addArgument(verbale)
                .log();
        return verbale;
    }

    @Override
    public DtoVerbale consolidaVerbale(String idVerbale, DtoProtocollo protocollo) {
        log.atDebug().setMessage("Richiesta consolidamento verbale {} con protocollo {}")
                .addArgument(idVerbale)
                .addArgument(protocollo)
                .log();

        DtoVerbale verbale = webClient
                .post()
                .uri("/verbali/{idVerbale}/protocollo", idVerbale)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(protocollo)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    String bodyAsString = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    log.atWarn().setMessage("Consolidamento verbale {} fallito, errore: {} {}")
                            .addArgument(idVerbale)
                            .addArgument(response.getStatusCode())
                            .addArgument(bodyAsString)
                            .log();
                    throw new UnsupportedOperationException("Error: " + bodyAsString);

                }))
                .body(DtoVerbale.class);
        log.atInfo().setMessage("Consolidamento verbale {}  con {} effettuata con successo: {}")
                    .addArgument(idVerbale)
                    .addArgument(protocollo)
                    .addArgument(verbale)
                    .log();
        return verbale;
    }

    @Override
    public DtoVerbale annullaVerbale(String idVerbale) {
        log.atDebug().setMessage("Richiesta annullamento verbale {} ")
                .addArgument(idVerbale)
                .log();

        DtoVerbale verbale = webClient
                .post()
                .uri("/verbali/{idVerbale}/annulla", idVerbale)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    String bodyAsString = StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    log.atWarn().setMessage("Annullamento verbale {} fallito, errore: {} {}")
                            .addArgument(idVerbale)
                            .addArgument(response.getStatusCode())
                            .addArgument(bodyAsString)
                            .log();
                    throw new UnsupportedOperationException("Error: " + bodyAsString);

                }))
                .body(DtoVerbale.class);
        // Log the successful response
        log.atInfo().setMessage("Annullamento verbale {}  effettuata con successo: {}")
                .addArgument(idVerbale)
                .addArgument(verbale)
                .log();
        return verbale;

    }

}
