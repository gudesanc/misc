package org.gds.poc.orch.ssm.vebalizzazione;

import jakarta.annotation.Resource;
import org.gds.poc.orch.ssm.Order;
import org.gds.poc.orch.ssm.OrderService;
import org.gds.poc.orch.ssm.client.verbale.DtoCreaVerbale;
import org.gds.poc.orch.ssm.client.verbale.DtoVerbale;
import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.BusinessStatus;
import org.gds.poc.orch.ssm.libreria.GenericStateMachineProcess;
import org.gds.poc.orch.ssm.libreria.PersistInMemoryHandler;
import org.gds.poc.orch.ssm.libreria.ProcessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VerbalizzazioneController {

    @Resource
    private final VerbaleService verbaleService;
    @Resource
    private final PersistInMemoryHandler persistInMemoryHandler;

    public VerbalizzazioneController(VerbaleService verbaleService, PersistInMemoryHandler persistInMemoryHandler) {
        this.verbaleService = verbaleService;
        this.persistInMemoryHandler = persistInMemoryHandler;
    }

    @PostMapping("/verbalizzazione")
    public DtoAvvioProcesso verbalizza(DtoCreaVerbale verbaleDaVerbalizzare){
        verbaleService.creaVerbale(verbaleDaVerbalizzare)
                .doOnSuccess( v -> {
                            String uuid = persistInMemoryHandler.createNewProcess(VerbalizzazioneController.create(v.id()));
                            persistInMemoryHandler.change(
                                    uuid,
                                    VerbalizzazioneStateMachineConfig.VerbalizzazioneChangeEventEnum.START.name(),
                                    BusinessStatus.RUNNING,
                                    null
                            );
                        }
                        );

/*                .doOnError()
        Order o =orderService.create();
        System.out.println("Order：" + o);
        return getResponse(o);*/
    }


    private static GenericStateMachineProcess create(String idVerbale){
        return new GenericStateMachineProcess(idVerbale,
                "verbalizzazione",
                    VerbalizzazioneStateMachineConfig.VerbalizzazioneStatusEnum.INIT.name(),
                "machineId",
                "endpoint",
                ProcessType.SYNC_ORCHESTRATION,
                null,
                null,
                BusinessStatus.RUNNING
                );
    }

}