package org.gds.poc.orch.ssm.libreria;

import jakarta.annotation.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

public abstract class GenericSateMachineController<T> {
    @Resource
    protected final PersistInMemoryHandler persistInMemoryHandler;

    protected GenericSateMachineController(PersistInMemoryHandler persistInMemoryHandler) {
        this.persistInMemoryHandler = persistInMemoryHandler;
    }


    protected String createNewProcess(String businessId, String intialState, String machineId,  String processName, String endpoint,T businessCtx, ProcessType processType, String parentUUID){
        String json = null;
        if(businessCtx!=null){
            json = BusinessContextJsonSerializer.serialize(businessCtx);
        }
        GenericStateMachineProcess process = new GenericStateMachineProcess(
                businessId,processName,intialState,machineId,endpoint,processType,parentUUID,json,BusinessStatus.RUNNING
        );
        return persistInMemoryHandler.createNewProcess(process);
    }

    protected void notificaEvento(String uuid, String event, T businessContext, BusinessStatus businessStatus){
        String json = null;
        if(businessContext!=null){
            json = BusinessContextJsonSerializer.serialize(businessContext);
        }
        persistInMemoryHandler
                .change(uuid,event,businessStatus,json);

    }


    @PostMapping(value="/events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Void> notifica(@RequestBody DTOEvent event){
        persistInMemoryHandler
                .change(event.uuid(),event.event(),event.businessStatus(),event.jsonBusinessContext());
        return Mono.empty();
    }
}
