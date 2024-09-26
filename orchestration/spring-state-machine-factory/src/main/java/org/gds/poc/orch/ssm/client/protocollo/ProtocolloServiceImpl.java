package org.gds.poc.orch.ssm.client.protocollo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Service
public class ProtocolloServiceImpl implements ProtocolloService {
    private final Logger log = LoggerFactory.getLogger(ProtocolloServiceImpl.class);
    private final RestClient webClient;

    public ProtocolloServiceImpl(RestClient.Builder webClientBuilder, @Value("${client.protocollo.endpoint}") String endpoint) {
        this.webClient = webClientBuilder.baseUrl(endpoint).build();
        log.atInfo().setMessage("Endpoint servizi protocollo: {}").addArgument(endpoint).log();
    }

    public DtoProtocollo protocolla(DtoCreaProtocollo infoCreazione){
        log.atDebug().setMessage("Richiesta protocollazione per: {}").addArgument(infoCreazione).log();
        DtoProtocollo result = webClient
                .post()
                .uri("/protocolli/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(infoCreazione)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    String bodyAsString =StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    log.atWarn().setMessage("Procollazione per: {} fallita, errore: {} - {}")
                            .addArgument(infoCreazione)
                            .addArgument(response.getStatusCode())
                            .addArgument(bodyAsString)
                            .log();
                    throw new UnsupportedOperationException("Error: " + bodyAsString);

                }))
                .body(DtoProtocollo.class);
        log.atInfo().setMessage("Procollazione per {} effettuata con successo: {}")
                .addArgument(infoCreazione)
                .addArgument(result)
                .log();
        return result;
    }
    public void annullaProtocollo(DtoProtocollo protocollo){
        log.atDebug().setMessage("Richiesta anullamento per: {}").addArgument(protocollo).log();

        webClient
                .method(HttpMethod.DELETE)
                .uri("/protocolli/")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(protocollo)
                .retrieve()
                .onStatus(HttpStatusCode::isError, ((request, response) -> {
                    String bodyAsString =StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8);
                    log.atWarn().setMessage("Errore annullamento protocollo {}, errore: {} - {}")
                            .addArgument(protocollo)
                            .addArgument(response.getStatusCode())
                            .addArgument(bodyAsString)
                            .log();
                    throw new UnsupportedOperationException("Error: " + bodyAsString);

                })).toBodilessEntity();
        log.atInfo().setMessage("Procollo {} annullato con successo")
                .addArgument(protocollo)
                .log();

    }

}
