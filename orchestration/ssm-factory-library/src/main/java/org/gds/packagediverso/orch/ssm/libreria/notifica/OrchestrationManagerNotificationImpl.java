package org.gds.packagediverso.orch.ssm.libreria.notifica;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.amqp.core.Queue;



@Component
public class OrchestrationManagerNotificationImpl implements OrchestrationManagerNotification {
    private static final String ROUTING_NUOVO_PROCESSO = "create";
    private static final String ROUTING_AGGIORNA_PROCESSO = "update";
    private final RabbitTemplate template;

    public OrchestrationManagerNotificationImpl(
            @Qualifier(value = "orchestrazioneRabbitTemplate") RabbitTemplate template) {
        this.template = template;
    }

    @Override
    @Async
    public void notificaNuovoProcesso(CreateOrchProcessRequest nuovoProcesso) {
        template.convertAndSend(ROUTING_NUOVO_PROCESSO,nuovoProcesso);

    }

    @Override
    @Async
    public void notificaCambioStato(UpdateOrchProcessRequest nuovoStato) {
        template.convertAndSend(ROUTING_AGGIORNA_PROCESSO,nuovoStato);
    }
}
