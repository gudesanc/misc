package org.gds.misc.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class XMLDocumentInfo {
    private final Document xmlDocument;
    private final XPathFactory xPathFactory;
    private final static Map<String,String> ns = new HashMap();
    static {
        ns.put("xm","http://www.eng.it/suap/XMLModelloUnico");
        ns.put("suap","http://www.eng.it/suap/suapschema");
    }

    public XMLDocumentInfo(InputStream xml) throws RuntimeException {
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            xmlDocument = builder.parse(new InputSource(xml));
            xPathFactory = XPathFactory.newInstance();
            System.out.println("XML input: "+toXMLString());
        } catch (Exception e) {
            throw new RuntimeException("Error parsing xml: " + e.getMessage());
        }
    }

    public Node getNode(String xPathExpression){
        return (Node) getXPathValure(xPathExpression, XPathConstants.NODE);

    }

    public NodeList getNodeList(String xPathExpression){
        return (NodeList) getXPathValure(xPathExpression,XPathConstants.NODESET);
    }



    private Object getXPathValure(String xPath, QName type){
        XPath x = xPathFactory.newXPath();
//        x.setNamespaceContext(new SimpleNamespaceContext(ns));
        try {
            return x.compile(xPath)
                    .evaluate(xmlDocument,type);
        } catch (XPathExpressionException e) {
            throw new RuntimeException("Errore creazione xpath: "+xPath+", errore: "+e);
        }
    }

    public String toXMLString() throws Exception{
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(xmlDocument), new StreamResult(writer));
        String output = writer.getBuffer().toString().replaceAll("\n|\r", "");
        return output;
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