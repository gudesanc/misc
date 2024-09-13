package org.gds.poc.orch.service.protocollo.protocollazione;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ProtocollazioneServiceImpl implements ProtocollazioneService{
    private final Logger log = LoggerFactory.getLogger(ProtocollazioneServiceImpl.class);
    private static final Map<Integer,Map<Integer, Protocollo>> protocolliPerAnno = new HashMap();
    private static final Set<Protocollo> protocolliAnnullati = new HashSet<>();
    @Override
    public Mono<Protocollo> creaProtocollo(DtoCreaProtocollo dto) {
        int anno = LocalDate.now().getYear();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            return Mono.error(new RuntimeException(e));
        }
        if(((int)(Math.random()*10000))%2==0){
            if(!protocolliPerAnno.containsKey(anno)){
                protocolliPerAnno.put(anno,new HashMap<>());
            }
            Map<Integer,Protocollo> protocolliCorrenti = protocolliPerAnno.get(anno);
            int next = (protocolliCorrenti.keySet().stream().reduce(Integer::max).orElse(0))+1;
            Protocollo p = new Protocollo("CA",anno,next);
            protocolliCorrenti.put(next,p);
            log.info("Protocollazione per {} effettuta: {}",dto,p);
            return Mono.just(p);
        }else{
            log.error("Errore nella protocollazione di: {}",dto);
            throw new UnsupportedOperationException("Errore nella protocollazione");
        }
    }

    @Override
    public Mono<Protocollo> annullaProtocollo(Protocollo p){
        return caricaProtocollo(p).
                doOnNext(loaded -> {
                    if (protocolliAnnullati.contains(loaded)) {
                        log.atInfo().setMessage("Trovato il protocollo {} tra quelli nnullati").addArgument(loaded).log();
                        throw new IllegalArgumentException("Il " + loaded + " già risulta annullato");
                    }
                })
                .doOnNext(protocollo -> {
                    protocolliAnnullati.add(protocollo);
                    log.info("Protocollo {} annullato", protocollo);
                })  ;
    }

    private Mono<Protocollo> caricaProtocollo(Protocollo p){
        Map<Integer,Protocollo> protocolliRiferimento = protocolliPerAnno.get(p.anno());
        if(protocolliRiferimento==null){
            throw new IllegalArgumentException("Nessun protocollo presente per l'anno "+p.anno());
        }
        Protocollo protocollo =protocolliRiferimento.get(p.progressivo());
        if(protocollo==null){
            throw new IllegalArgumentException("Protocollo "+p+" non presente");
        }
        return Mono.just(protocollo);
    }
}
