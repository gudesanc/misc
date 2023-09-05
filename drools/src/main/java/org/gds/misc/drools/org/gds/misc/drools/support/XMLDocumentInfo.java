package org.gds.misc.drools.org.gds.misc.drools.support;

import io.quarkus.logging.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLDocumentInfo implements SchemaConstants{
    private final Document xmlDocument;
    private final XPathFactory xPathFactory;
    private final static Map<String,String> ns = new HashMap();

    static {
        ns.put("xm",NS_MODELLO_UNICO);
        ns.put("suap",NS_SUAP);
    }

    public XMLDocumentInfo(String xml) throws RuntimeException {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            dbFactory.setNamespaceAware(true);
            DocumentBuilder builder = dbFactory.newDocumentBuilder();
            xmlDocument = builder.parse(new InputSource(new StringReader(xml)));
            xPathFactory = XPathFactory.newInstance();
            Log.tracef("XML input: %s",xml);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing xml: " + e.getMessage());
        }
    }

    public Node getNode(String xPathExpression){
        return (Node) getXPathObj(xPathExpression,XPathConstants.NODE);

    }

    public NodeList getNodeList(String xPathExpression){
        return (NodeList) getXPathObj(xPathExpression,XPathConstants.NODESET);
    }



    private Object getXPathObj(String xPath, QName type){
        XPath x = xPathFactory.newXPath();
        x.setNamespaceContext(new SimpleNamespaceContext(ns));
        try {
            return x.compile(xPath)
                    .evaluate(xmlDocument,type);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Errore creazione xpath: "+xPath+", errore: "+e);
        }
    }
}
class SimpleNamespaceContext implements NamespaceContext {

    private final Map<String, String> PREF_MAP = new HashMap<String, String>();

    public SimpleNamespaceContext(final Map<String, String> prefMap) {
        PREF_MAP.putAll(prefMap);
    }

    public String getNamespaceURI(String prefix) {
        return PREF_MAP.get(prefix);
    }

    public String getPrefix(String uri) {
        throw new UnsupportedOperationException();
    }

    public Iterator getPrefixes(String uri) {
        throw new UnsupportedOperationException();
    }
}