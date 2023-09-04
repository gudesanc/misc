package org.gds.misc.drools.units;

import io.quarkus.logging.Log;
import org.drools.ruleunits.api.RuleUnitData;
import org.gds.misc.drools.model.TipoPratica;
import org.gds.misc.drools.model.Validazione;
import org.gds.misc.drools.org.gds.misc.drools.support.XMLDocumentInfo;

public class ValidazioneUnit implements RuleUnitData {
    private String xml;
    private TipoPratica tipoPratica;
    private Validazione validazione = new Validazione();
    private XMLDocumentInfo xmlDocument;
    private final Object lock = new Object();


    public ValidazioneUnit() {

    }




    public Validazione getValidazione(){
        return validazione;
    }

    public String getXml() {
        return xml;
    }

    public TipoPratica getTipoPratica() {
        return tipoPratica;
    }

    public XMLDocumentInfo getXmlDocument(){
        Log.debug("Richiesta  XML Document");
        synchronized (lock){
            if(xmlDocument==null){
                long start = System.currentTimeMillis();
                xmlDocument = new XMLDocumentInfo(xml);
                long end = System.currentTimeMillis();
                Log.infof("Creato XML Document e XPathFactory %d (ms)",(end-start));
            }
            return xmlDocument;
        }
    }


}
