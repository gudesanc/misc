package org.acme.domain;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.List;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

@Path("/entities")
public class MyEntityResource {
    Logger log = LoggerFactory.getLogger(MyEntityResource.class);
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyEntity> getAll() {
        log.debug("Inizio richiesta caricamento ");
        PanacheQuery<MyEntity> list100 = MyEntity.findAll();
        list100.page(1,100);
        List<MyEntity> result = list100.list();
        log.info("Caricati: {} record",result.size());
        return result;
    }
    @GET
    @Path("/{id}")
    public MyEntity get(Long id) {
        log.debug("Inizio richiesta caricamento risorsa {}",id);
        return MyEntity.findById(id);
    }

    @POST
    @Transactional
    public Response create(MyEntity myentities) {
        myentities.persist();
        log.info("Persistito record: {} ",myentities);
        return Response.created(URI.create("/myentities/" + myentities.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public MyEntity update(Long id, MyEntity myentities) {
        MyEntity entity = MyEntity.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }

        // map all fields from the person parameter to the existing entity
        entity.field = myentities.field;

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void delete(Long id) {
        MyEntity entity = MyEntity.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }
        entity.delete();
    }



    @GET
    @Path("/count")
    public Long count() {
        return MyEntity.count();
    }
}
