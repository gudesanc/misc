package org.acme;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.acme.domain.MyEntityDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@Path("/entities")
@RegisterRestClient
public interface MyEntityRestClient {


    @GET
    public List<MyEntityDTO> getAll();
}
