package org.gds.poc.orch.service.protocollo.protocollazione;

import org.gds.poc.orch.domain.jpl.common.Protocollo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ProtocollazioneServiceImpl implements ProtocollazioneService{
    private final Logger log = LoggerFactory.getLogger(ProtocollazioneServiceImpl.class);
    private static final Map<Integer,Map<Integer, Protocollo>> protocolliPerAnno = new HashMap<>();
    private static final Set<Protocollo> protocolliAnnullati = new HashSet<>();
    @Override
    public Protocollo creaProtocollo(DtoCreaProtocollo dto) {
        int anno = LocalDate.now().getYear();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw  new RuntimeException(e);
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
            return p;
        }else{
            log.error("Errore nella protocollazione di: {}",dto);
            throw new UnsupportedOperationException("Errore nella protocollazione");
        }
    }

    @Override
    public Protocollo annullaProtocollo(Protocollo p){
        Protocollo savedProtocollo = caricaProtocollo(p);
            if (protocolliAnnullati.contains(savedProtocollo)) {
                        log.atInfo().setMessage("Trovato il protocollo {} tra quelli nnullati").addArgument(savedProtocollo).log();
                        throw new IllegalArgumentException("Il " + savedProtocollo + " già risulta annullato");
            }
            protocolliAnnullati.add(savedProtocollo);
            log.info("Protocollo {} annullato", savedProtocollo);
            return savedProtocollo;
    }

    private Protocollo caricaProtocollo(Protocollo p){
        Map<Integer,Protocollo> protocolliRiferimento = protocolliPerAnno.get(p.anno());
        if(protocolliRiferimento==null){
            throw new IllegalArgumentException("Nessun protocollo presente per l'anno "+p.anno());
        }
        Protocollo protocollo =protocolliRiferimento.get(p.progressivo());
        if(protocollo==null){
            throw new IllegalArgumentException("Protocollo "+p+" non presente");
        }
        return protocollo;
    }
}
