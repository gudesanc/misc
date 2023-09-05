package org.gds.misc.drools.org.gds.misc.drools.support;

import io.quarkus.logging.Log;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class  XPathHelper implements SchemaConstants{




    /**
     * Metodo per ottenere il valore di un campo ) all'interno di una dichiarazione
     * @param xmlDocumentInfo l'xml
     * @param codiceIntervento il codice dichiarazione (opzionale)
     * @param codiceDichiarazione il codice della dichiarazione (opzionale)
     * @param campo il nome del campo
     * @return il valore...
     */
    public static Object ottieniValoreDaCampoInDichiarazione(XMLDocumentInfo xmlDocumentInfo, String codiceIntervento, String codiceDichiarazione, String campo) {
        String xPathRoot = "/xm:modelloUnico/xm:procedimento//";
        if (codiceIntervento != null)
            xPathRoot += "xm:intervento[@codice='" + codiceIntervento + "']//";

        if (codiceDichiarazione != null)
            xPathRoot += "xm:dichiarazione[@codice='" + codiceDichiarazione + "']//";
        else
            xPathRoot += "xm:dichiarazione//";

        String xPath = xPathRoot + "suap:field[@name='" + campo + "']";
        Log.tracef("Avvia ricerca campo,  xPath: %s",xPath);
        Element element = getFirstElement(xmlDocumentInfo.getNodeList(xPath));
        Object value = estraiValoreDaCampo(element);
        Log.infof("Avvia ricerca campo,  xPath: %s, value: %s",xPath,value);
        return value;
    }


    /**
     * Metodo per estarre il valore da un field di tipo "standard"
     * @param field il campo
     * @return
     */
    private static Object estraiValoreDaCampo(Element field) {
        if(field!=null) {
            String rawValue = getSubElementTextContent(field, NS_SUAP, SUAP_FIELD_VALUE_EL_NAME);
            String rawType = field.getAttributeNS(NS_SUAP, SUAP_FIELD_TYPE_ATTR_NAME);
            if (rawValue != null && rawType != null) {
                FieldType ft = FieldType.valueOf(rawType);
                switch (ft) {
                    case NUMERIC:
                        String pattern = getSubElementTextContent(field, NS_SUAP, SUAP_FIELD_PATTERN_EL_NAME);
                        if (pattern != null && (
                                pattern.equals(PATTERN_DUE_DECIMALI) ||
                                        pattern.equals(PATTERN_TRE_DECIMALI) ||
                                        pattern.equals(PATTERN_DECIMALI_LIBERI)

                        )) {
                            return Double.parseDouble(rawValue.replace(",", "."));
                        }
                        return Integer.parseInt(rawValue);
                    default:
                        return rawValue;
                }
            }
        }
        return null;
    }

    /**
     * Metodo d'ausilio per estarre il text-content da un sotto elemento
     * @param parent l'elemento padre
     * @param namespaceUri il namespace del sotto elemento
     * @param elementName il nome del sotto elemento
     * @return il text-content... o null se non presente
     */
    private static String getSubElementTextContent(Element parent, String namespaceUri, String elementName ){
        NodeList nl = parent.getElementsByTagNameNS(namespaceUri, elementName);
        if(nl.getLength()>0){
            return nl.item(0).getTextContent();
        }
        return null;
    }

    /**
     * MEtodo d'ausilio che prova ad estrare da un nodelist il primo element..
     * @param nodeList  il node list dal quale estrarre l'elemento
     * @return l'element o null
     */
    private static Element getFirstElement(NodeList nodeList){
        if(nodeList==null
                || nodeList.getLength()<1
                || nodeList.item(0)==null
                || (Node.ELEMENT_NODE != nodeList.item(0).getNodeType())
        ){
            return null;
        }
        return (Element) nodeList.item(0);
    }
}
