package org.gds.misc.drools.units;

import io.quarkus.logging.Log;
import org.drools.ruleunits.api.RuleUnitData;
import org.gds.misc.drools.model.fact.TipoPratica;
import org.gds.misc.drools.org.gds.misc.drools.support.XMLDocumentInfo;

/**
 * ...tutte le nostre rule unit hanno in input il tipo pratica e l'xml
 */
public class AbstractRuleUnit implements RuleUnitData {
    private String xml;
    private TipoPratica tipoPratica;
    private XMLDocumentInfo xmlDocument;
    private final Object lock = new Object();

    public void setXml(String xml) {
        this.xml = xml;
    }

    public void setTipoPratica(TipoPratica tipoPratica) {
        this.tipoPratica = tipoPratica;
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
