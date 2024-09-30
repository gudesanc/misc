package org.gds.poc.orch.ssm.notificazione;

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
@RequestMapping("/notificazione")
public class NotificazioneController extends GenericSateMachineController<NotificazioneContext,Void>{

    public NotificazioneController(
                                     @Qualifier( "notificazione") StateMachineFactory<String,String> factory) {
        super(factory);
    }

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public DtoAvvioProcesso notifica(DtoAvvioNotificazione notificazione){

        NotificazioneContext ctx = new NotificazioneContext();
        ctx.setIdNotifica("Not"+System.currentTimeMillis());
        ctx.setOggettoNotifica(notificazione.oggettoNotifica());
        AvviaStateMachineRequest<NotificazioneContext> request = new
                AvviaStateMachineRequest<>(
                NotificazioneStateMachineConfig.NotificazioneStateEnum.INIT.name(),
                "machineId",
                "verbalizzazione",
                "endpoint",
                ctx,
                ProcessType.PSEUDO_SYNCH,null
        );

        String uuid = createNewProcess(request, NotificazioneStateMachineConfig.NotificazioneEventEnum.START.name());
        return new DtoAvvioProcesso(uuid);

    }




}