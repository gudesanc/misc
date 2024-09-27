package org.gds.poc.orch.manager;

import org.gds.poc.orch.manager.library.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class OrchestrationProcessServiceImpl implements OrchestrationProcessService{
    private static final Logger LOG = LoggerFactory.getLogger(OrchestrationProcessService.class);
    private final OrchestrationProcessRepository  processRepository;
    private final OrchestrationProcessHistoryRepository  historyRepository;

    public OrchestrationProcessServiceImpl(OrchestrationProcessRepository processRepository, OrchestrationProcessHistoryRepository historyRepository) {
        this.processRepository = processRepository;
        this.historyRepository = historyRepository;
    }

    public void createNewProcess(CreateOrchProcessRequest nuovoProcesso){
        String user = nuovoProcesso.user()!=null? nuovoProcesso.user():
                "SYSTEM";
        OrchestrationProcess process = new OrchestrationProcess(
                nuovoProcesso.uuid(),nuovoProcesso.tenantId(),nuovoProcesso.ente(),
                user,nuovoProcesso.area(),nuovoProcesso.procedura(),
                nuovoProcesso.stateMachineID(),nuovoProcesso.domainOrchEndpoint(),
                nuovoProcesso.stateMachineInitialState(), nuovoProcesso.creationTimestamp(),
                nuovoProcesso.processType(),BusinessState.RUNNING,nuovoProcesso.parentUuid(),
                nuovoProcesso.ctx()
        );
        processRepository.save(process);
        LOG.atInfo().setMessage("Nuovo Processo Salvato: {}").addArgument(process).log();
    }

    public void aggiornaStatoProcesso(UpdateOrchProcessRequest aggiornamentoProcesso){
        LOG.atDebug().setMessage("Richiesta di aggioramento processo: {}")
                .addArgument(aggiornamentoProcesso)
                .log();
        Optional<OrchestrationProcess> oProcesso = processRepository.findById(aggiornamentoProcesso.uuid());
        if(oProcesso.isEmpty()){
            manageProcessNotFound(aggiornamentoProcesso.uuid());
        }else {
            OrchestrationProcess processo = oProcesso.get();
            LOG.atDebug().setMessage("Processo: {} caricato")
                    .addArgument(processo)
                    .log();
            if(processo.getBusinessState().isFinalState()){
                LOG.atWarn().setMessage("Il Processo {} si trova già in uno stato finale: {}")
                        .addArgument(processo.getUuid())
                        .addArgument(processo)
                        .log();
                //TODO: dobbiamo lanciare eccezione? ignoriamo silently la richiesta?

            }
            String startSMState = processo.getStateMachinCurrentState();
            BusinessState startBusinessState = processo.getBusinessState();
            if(aggiornamentoProcesso.stateMachineState()!=null){
                processo.setStateMachinCurrentState(aggiornamentoProcesso.stateMachineState());
            }
            if(aggiornamentoProcesso.businessState()!=null){
                processo.setBusinessState(aggiornamentoProcesso.businessState());
            }
            if(aggiornamentoProcesso.ctx()!=null){
                processo.setContext(aggiornamentoProcesso.ctx());
            }
            if(aggiornamentoProcesso.result()!=null){
                processo.setResult(aggiornamentoProcesso.result());
            }
            if(processo.getBusinessState().isFinalState()){
                processo.setEndTime(aggiornamentoProcesso.eventTimestamp());
            }
            processRepository.save(processo);
            LOG.atDebug().setMessage("Processo: {} aggiornato")
                    .addArgument(processo)
                    .log();
            OrchestrationProcessHistory history = new OrchestrationProcessHistory(
                    aggiornamentoProcesso.uuid(),aggiornamentoProcesso.eventTimestamp(),
                    startBusinessState,processo.getBusinessState(),startSMState,processo.getStateMachinCurrentState());
            historyRepository.save(history);
            LOG.atDebug().setMessage("Record history: {} inserito")
                    .addArgument(processo)
                    .log();
            LOG.atInfo().setMessage("Processo {} aggiornato, SM state corrente: {}, stato corrento Business: {}")
                    .addArgument(processo.getUuid())
                    .addArgument(processo.getStateMachinCurrentState())
                    .addArgument(processo.getBusinessState())
                    .log();
        }
    }

    public BusinessState getBusinessState(String uuid){
        LOG.atDebug().setMessage("Richiesta informazioni stato Business processo: {}")
                .addArgument(uuid)
                .log();
        List<BusinessState> state = processRepository.getBusinessStateByUuid(uuid);
        if(state.isEmpty()) {
            manageProcessNotFound(uuid);
            return null; //Ma in realtà lancia eccezione... santa paziena
        }else {
            BusinessState result = state.getFirst();
            LOG.atInfo().setMessage("Stato Business Processo {} recuperato {}")
                    .addArgument(uuid)
                    .addArgument(result)
                    .log();
            return result;
        }

    }

    public String getResult(String uuid){
        LOG.atDebug().setMessage("Richiesta informzioni risultato processo: {}")
                .addArgument(uuid)
                .log();
        List<String> oResult = processRepository.getRestultByUuid(uuid);
        if(oResult.isEmpty()) {
            manageProcessNotFound(uuid);
            return null; //Ma in realtà lancia eccezione... santa paziena
        }else {
            String result = oResult.getFirst();
            LOG.atInfo().setMessage("Risulato Processo {} recuperato {}")
                    .addArgument(uuid)
                    .addArgument(result)
                    .log();
            return result;
        }
    }

    @Override
    public ProcessDetailResponse getDetail(String uuid) {
        Optional<OrchestrationProcess> oProcess = processRepository.findById(uuid);
        if(oProcess.isEmpty()){
            manageProcessNotFound(uuid);
            return null; //Ma in realtà lancia eccezione... santa paziena
        }else {
            OrchestrationProcess process = oProcess.get();
            List<ProcessEventDetail> eventi = historyRepository.findByProcessUUIDOrderByEventTime(uuid)
                    .stream().map(e -> new ProcessEventDetail(
                            e.getEventTime(),
                            e.getBusinessStateSource(), e.getBusinessStateTarget(),
                            e.getStateMachineSourceState(), e.getStateMachineTargetState())).toList();
            return  new ProcessDetailResponse(
                    process.getUuid(),
                    process.getBusinessState(),
                    process.getStateMachinCurrentState(),
                    process.getContext(),
                    process.getResult(),
                    process.getStartTime(),
                    process.getEndTime(),
                    eventi);
        }
    }

    private void manageProcessNotFound(String uuid){
        LOG.atWarn().setMessage("Processo {} non trovato")
                .addArgument(uuid)
                .log();
        throw new IllegalArgumentException("Processo con uuid "+uuid+" non presente");
    }
}
