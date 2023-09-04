package org.gds.misc.drools.org.gds.misc.drools.support;

import io.quarkus.logging.Log;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class  XPathHelper {
    public static boolean verifica(String xml, String c){
        Log.debugf("Avvio verifica xml:  %s, c: %s",new Object[]{xml,c});
        return c!=null && xml!=null;
    }


    public static Object ottieniValoreDaCampoInDichiarazione(XMLDocumentInfo xmlDocumentInfo, String codiceIntervento, String codiceDichiarazione, String campo) {
        String xPathRoot = "/xm:modelloUnico/xm:procedimento//";
        if (codiceIntervento != null)
            xPathRoot += "xm:intervento[@codice='" + codiceIntervento + "']//";

        if (codiceDichiarazione != null)
            xPathRoot += "xm:dichiarazione[@codice='" + codiceDichiarazione + "']//";
        else
            xPathRoot += "xm:dichiarazione//";

        String xPath = xPathRoot + "suap:field[@name='" + campo + "']";
        Log.infof("Avvia ricerca campo,  xPath: %s",xPath);
        NodeList nodeList = xmlDocumentInfo.getNodeList(xPath);
        Log.info("Node [0] "+nodeList.item(0));
        Node nodo = xmlDocumentInfo.getNode(xPath);
        Log.info("Nodo "+nodo);
        if(nodeList.getLength()<1) {
            return null;
        }
        else{
            return nodeList.getLength();
        }

    }
}
