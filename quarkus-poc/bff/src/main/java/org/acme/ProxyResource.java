package org.acme;

import exception.JEnteExceptionFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.MyEntityDTO;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/bff")
public class ProxyResource {
    private static final Logger logger = LoggerFactory.getLogger(ProxyResource.class);
    @RestClient
    private MyEntityRestClient restClient;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyEntityDTO> getAll() {
        try {
            return restClient.getAll();
        }catch (Throwable t){
            String codiceFiscale = "prova";
            logger.warn("SOAP Service invocato cf {} ma errore di richiesta non autorizzata: {}", codiceFiscale, t, t);
            return new ArrayList<>();
        }
    }

    @GET
    @Path("ohm")
    public String prova(@QueryParam("cosa") String valore) {
        Optional<Enum<SALUTI>> res = JEnteExceptionFactory.getValoreAsEnum(valore, SALUTI.class);
        return res.map(Enum::name).orElse("NIENTE");
    }

   enum SALUTI{
        ciao,buon_giorno,basta;
   }

}
