package org.acme;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.domain.MyEntityDTO;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@Path("/bff")
public class ProxyResource {

    @RestClient
    private MyEntityRestClient restClient;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyEntityDTO> getAll() {
        return restClient.getAll();
    }
}
