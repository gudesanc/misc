package org.gds.poc.orch.ssm.notificazione;

import jakarta.annotation.Resource;
import org.gds.poc.orch.ssm.libreria.StateMachineLogListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableStateMachineFactory(name = "notificazione")
public class NotificazioneStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {
    @Resource
    private StateMachineLogListener stateMachineLogListener;
    @Resource
    private NotificazioneAcquisciProtocolloAction notificazioneAcquisciProtocolloAction;
    @Resource
    private NotificazioneAnnullaProtocolloAction notificazioneAnnullaProtocolloAction;
    @Resource
    private NotificazionePersistiProtocolloAction notificazionePersistiProtocolloAction;
    @Resource
    private NotificazioneAnnullaVerbaleAction notificazioneAnnullaVerbaleAction;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config)
            throws Exception {
        config
                .withConfiguration()
                .machineId("notificazione")
                .listener(stateMachineLogListener);
    }
    /**
     * configure state
     * NON METTERE STATI "END" ALTRIMENTI FINISCE LA STATE MACHINE ED AMEN :)
     */
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(NotificazioneStatusEnum.INIT.name())
                .state(NotificazioneStatusEnum.NOTIFICA_IN_BOZZA.name(),notificazioneAcquisciProtocolloAction)
                .state(NotificazioneStatusEnum.NOTIFICA_DA_PERSISTERE_PROTOCOLLO.name(),notificazionePersistiProtocolloAction)
                .state(NotificazioneStatusEnum.NOTIFICA_DA_ANNULLARE_PROTOCOLLO.name(),notificazioneAnnullaProtocolloAction)
                .state(NotificazioneStatusEnum.DA_ANNULLARE_NOTIFICA.name(),notificazioneAnnullaVerbaleAction)
                .end(NotificazioneStatusEnum.NOTIFICA_CONSOLIDATO.name())
                .end(NotificazioneStatusEnum.NOTIFICA_ANNULLATA.name())
                .end(NotificazioneStatusEnum.NOTIFICA_PROTOCOLLO_ANNULLATO.name())
                .states(Arrays.stream(NotificazioneStatusEnum.values()).map(NotificazioneStatusEnum::name).collect(Collectors.toSet()));
    }

    /**
     * configure state transient  with event
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source(NotificazioneStatusEnum.INIT.name()).target(NotificazioneStatusEnum.NOTIFICA_IN_BOZZA.name())
                    .event(NotificazioneChangeEventEnum.START.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.NOTIFICA_IN_BOZZA.name()).target(NotificazioneStatusEnum.NOTIFICA_DA_PERSISTERE_PROTOCOLLO.name())
                    .event(NotificazioneChangeEventEnum.NOTIFICA_PROTOCOLLO_ACQUISITO.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.NOTIFICA_IN_BOZZA.name()).target(NotificazioneStatusEnum.DA_ANNULLARE_NOTIFICA.name())
                    .event(NotificazioneChangeEventEnum.NOTIFICA_PROTOCOLLO_NON_ACQUISTO.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.NOTIFICA_DA_PERSISTERE_PROTOCOLLO.name()).target(NotificazioneStatusEnum.NOTIFICA_CONSOLIDATO.name())
                    .event(NotificazioneChangeEventEnum.PROTOCOLLO_IMPOSTATO_SU_NOTIFICA.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.NOTIFICA_DA_PERSISTERE_PROTOCOLLO.name()).target(NotificazioneStatusEnum.DA_ANNULLARE_NOTIFICA.name())
                    .event(NotificazioneChangeEventEnum.PRTOCOLLO_NON_IMPOSTATO_SU_NOTIFICA.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.DA_ANNULLARE_NOTIFICA.name()).target(NotificazioneStatusEnum.NOTIFICA_ANNULLATA.name())
                    .event(NotificazioneChangeEventEnum.CAMBIATO_STATO_NOTIFICA.name())
                .and().withExternal()
                .source(NotificazioneStatusEnum.NOTIFICA_DA_ANNULLARE_PROTOCOLLO.name()).target(NotificazioneStatusEnum.NOTIFICA_PROTOCOLLO_ANNULLATO.name())
                    .event(NotificazioneChangeEventEnum.CAMBIATO_STATO_NOTIFICA.name())
        ;
    }

    public enum NotificazioneStatusEnum{
        INIT,
        NOTIFICA_IN_BOZZA,
        NOTIFICA_DA_PERSISTERE_PROTOCOLLO,
        NOTIFICA_CONSOLIDATO,
        DA_ANNULLARE_NOTIFICA,
        NOTIFICA_ANNULLATA,
        NOTIFICA_DA_ANNULLARE_PROTOCOLLO,
        NOTIFICA_PROTOCOLLO_ANNULLATO
    }

    public enum NotificazioneChangeEventEnum{
        START,
        NOTIFICA_PROTOCOLLO_ACQUISITO,
        NOTIFICA_PROTOCOLLO_NON_ACQUISTO,
        PROTOCOLLO_IMPOSTATO_SU_NOTIFICA,
        PRTOCOLLO_NON_IMPOSTATO_SU_NOTIFICA,
        CAMBIATO_STATO_NOTIFICA,
        NOTIFICA_CAMBIATO_STATO_PROTOCOLLO
    }



}