package org.gds.poc.orch.ssm.vebalizzazione;

import jakarta.annotation.Resource;
import org.gds.poc.orch.ssm.client.verbale.DtoCreaVerbale;
import org.gds.poc.orch.ssm.client.verbale.DtoVerbale;
import org.gds.poc.orch.ssm.client.verbale.VerbaleService;
import org.gds.poc.orch.ssm.libreria.AvviaStateMachineRequest;
import org.gds.poc.orch.ssm.libreria.GenericSateMachineController;
import org.gds.poc.orch.ssm.libreria.ProcessType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verbalizzazioni")
public class VerbalizzazioneController extends GenericSateMachineController<VerbalizzazioneContext>{

    @Resource
    private final VerbaleService verbaleService;
    public VerbalizzazioneController(VerbaleService verbaleService,
                                     @Qualifier( "verbalizzazione") StateMachineFactory<String,String> factory) {
        super(factory);
        this.verbaleService = verbaleService;
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DtoAvvioProcesso verbalizza(DtoCreaVerbale verbaleDaVerbalizzare){

        DtoVerbale nuovoVerbale = verbaleService.creaVerbale(verbaleDaVerbalizzare);
        VerbalizzazioneContext ctx = new VerbalizzazioneContext();
        ctx.setIdVerbale(nuovoVerbale.id());
        ctx.setOggettoVerbale(verbaleDaVerbalizzare.oggetto());
        AvviaStateMachineRequest<VerbalizzazioneContext> request = new
                AvviaStateMachineRequest<>(
                VerbalizzazioneStateMachineConfig.VerbalizzazioneStateEnum.INIT.name(),
                "machineId",
                "verbalizzazione",
                "endpoint",
                ctx,
                ProcessType.SYNC_ORCHESTRATION,null
        );

        String uuid = createNewProcess(request, VerbalizzazioneStateMachineConfig.VerbalizzazioneEventEnum.START.name());
        return new DtoAvvioProcesso(uuid);

    }




}