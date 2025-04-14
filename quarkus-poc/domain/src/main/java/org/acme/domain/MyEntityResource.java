package org.acme.domain;

import exception.JEnteConfigurationException;
import exception.JEnteExceptionFactory;
import jakarta.inject.Inject;
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

    private final MyEntityMapper mapper;

    public MyEntityResource(MyEntityMapper mapper) {
        this.mapper = mapper;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MyEntityDTO> getAll() {
        log.debug("Inizio richiesta caricamento ");
        PanacheQuery<MyEntity> list100 = MyEntity.findAll();
        list100.page(0,100);
        List<MyEntity> result = list100.list();
        log.info("Caricati: {} record",result.size());
        if(true){
            throw new JEnteConfigurationException("Errore di configurazione");
        }
        return mapper.toDTOList(result);
    }
    @GET
    @Path("/{id}")
    public MyEntityDTO get(Long id) {
        log.debug("Inizio richiesta caricamento risorsa {}",id);
        return mapper.toDTO(MyEntity.findById(id));
    }

    @POST
    @Transactional
    public Response create(MyEntityDTO dto) {
        MyEntity myentities = mapper.toEntity(dto);
        myentities.persist();
        log.info("Persistito record: {} ",myentities);
        return Response.created(URI.create("/myentities/" + myentities.id)).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public MyEntityDTO update(Long id, MyEntityDTO myentities) {
        MyEntity entity = MyEntity.findById(id);
        if(entity == null) {
            throw new NotFoundException();
        }

        // map all fields from the person parameter to the existing entity
        entity.field = myentities.field();

        return mapper.toDTO(entity);
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
