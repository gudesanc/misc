package org.gds.poc.orch.ssm.vebalizzazione;

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
@EnableStateMachineFactory(name = "verbalizzazione")
public class VerbalizzazioneStateMachineConfig extends StateMachineConfigurerAdapter<String, String> {
    @Resource
    private StateMachineLogListener stateMachineLogListener;    @Resource
    private AcquisciProtocolloAction acquisciProtocolloAction;
    @Resource
    private AnnullaProtocolloAction annullaProtocolloAction;
    @Resource
    private PersistiProtocolloAction persistiProcolloAction;
    @Resource
    private AnnullaVerbaleAction annullaVerbaleAction;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config)
            throws Exception {
        config
                .withConfiguration()
                .machineId("orch-verbalizzazione")
                .listener(stateMachineLogListener);
    }
    /**
     * configure state
     * NON METTERE STATI "END" ALTRIMENTI FINISCE LA STATE MACHINE ED AMEN :)
     */
    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states.withStates()
                .initial(VerbalizzazioneStatusEnum.INIT.name())
                .state(VerbalizzazioneStatusEnum.VERBALE_IN_BOZZA.name(),acquisciProtocolloAction)
                .state(VerbalizzazioneStatusEnum.DA_PERSISTERE_PROTOCOLLO.name(),persistiProcolloAction)
                .state(VerbalizzazioneStatusEnum.DA_ANNULLARE_PROTOCOLLO.name(),annullaProtocolloAction)
                .state(VerbalizzazioneStatusEnum.DA_ANNULLARE_VERBALE.name(),annullaVerbaleAction)
                .end(VerbalizzazioneStatusEnum.VERBALE_CONSOLIDATO.name())
                .end(VerbalizzazioneStatusEnum.VERBALE_ANNULLATO.name())
                .end(VerbalizzazioneStatusEnum.PROTOCOLLO_ANNULLATO.name())
                .states(Arrays.stream(VerbalizzazioneStatusEnum.values()).map(VerbalizzazioneStatusEnum::name).collect(Collectors.toSet()));
    }

    /**
     * configure state transient  with event
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source(VerbalizzazioneStatusEnum.INIT.name()).target(VerbalizzazioneStatusEnum.VERBALE_IN_BOZZA.name())
                    .event(VerbalizzazioneChangeEventEnum.START.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.VERBALE_IN_BOZZA.name()).target(VerbalizzazioneStatusEnum.DA_PERSISTERE_PROTOCOLLO.name())
                    .event(VerbalizzazioneChangeEventEnum.PROTOCOLLO_ACQUISITO.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.VERBALE_IN_BOZZA.name()).target(VerbalizzazioneStatusEnum.DA_ANNULLARE_VERBALE.name())
                    .event(VerbalizzazioneChangeEventEnum.PROTOCOLLO_NON_ACQUISTO.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.DA_PERSISTERE_PROTOCOLLO.name()).target(VerbalizzazioneStatusEnum.VERBALE_CONSOLIDATO.name())
                    .event(VerbalizzazioneChangeEventEnum.PROTOCOLLO_IMPOSTATO_SU_VERBALE.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.DA_PERSISTERE_PROTOCOLLO.name()).target(VerbalizzazioneStatusEnum.DA_ANNULLARE_PROTOCOLLO.name())
                    .event(VerbalizzazioneChangeEventEnum.PRTOCOLLO_NON_IMPOSTATO_SU_VERBALE.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.DA_ANNULLARE_VERBALE.name()).target(VerbalizzazioneStatusEnum.VERBALE_ANNULLATO.name())
                    .event(VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_VERBALE.name())
                .and().withExternal()
                .source(VerbalizzazioneStatusEnum.DA_ANNULLARE_PROTOCOLLO.name()).target(VerbalizzazioneStatusEnum.PROTOCOLLO_ANNULLATO.name())
                    .event(VerbalizzazioneChangeEventEnum.CAMBIATO_STATO_PROTOCOLLO.name())
        ;
    }

    public enum VerbalizzazioneStatusEnum{
        INIT,
        VERBALE_IN_BOZZA,
        DA_PERSISTERE_PROTOCOLLO,
        VERBALE_CONSOLIDATO,
        DA_ANNULLARE_VERBALE,
        VERBALE_ANNULLATO,
        DA_ANNULLARE_PROTOCOLLO,
        PROTOCOLLO_ANNULLATO
    }

    public enum VerbalizzazioneChangeEventEnum{
        START,
        PROTOCOLLO_ACQUISITO,
        PROTOCOLLO_NON_ACQUISTO,
        PROTOCOLLO_IMPOSTATO_SU_VERBALE,
        PRTOCOLLO_NON_IMPOSTATO_SU_VERBALE,
        CAMBIATO_STATO_VERBALE,
        CAMBIATO_STATO_PROTOCOLLO
    }



}