package org.gds.misc.xpath;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;


public class XPathTesterMain implements SchemaConstants{

    public static void main(String[] args) {
        InputStream is = XPathTesterMain.class.getClassLoader().getResourceAsStream("a.xml");
        XMLDocumentInfo xmlDocumentInfo = new XMLDocumentInfo(is);
//        String xpath = "/Tutorials/Tutorial";
        String xpath="/xm:modelloUnico/xm:procedimento//xm:intervento[@codice='IndicazioneUbicazioneRiferimento']//xm:dichiarazione[@codice='IndirizzoRoma']//suap:field[@name='desVia']";
//        String xpath="/modelloUnico/procedimento/intervento[@codice='IndicazioneUbicazioneRiferimento']/dichiarazione[@codice='IndirizzoRoma']//field[@name='desVia']";
//        System.out.println(xmlDocumentInfo.getNode(xpath));
        NodeList nodeList = xmlDocumentInfo.getNodeList(xpath);
        System.out.println("Nodelist element: "+nodeList.getLength());
        for(int i = 0; i < nodeList.getLength(); i++){
            checkNode(nodeList.item(i));
        }
    }

    private static void checkNode(Node n){
        if(n==null){
            System.out.println("NULLL");
        }
        System.out.println("Node type: "+n.getNodeType()+", node name: "+n.getNodeName()+", node value: "+n.getNodeValue()+", node attribute: "+n.getAttributes()+".");
        if(Node.ELEMENT_NODE == n.getNodeType()){
            Element e = (Element) n;
            System.out.println("Field Type: "+e.getAttributeNS(NS_SUAP,SUAP_FIELD_TYPE_ATTR_NAME));
            System.out.println(estraiValore(e,e.getAttributeNS(NS_SUAP,SUAP_FIELD_TYPE_ATTR_NAME)));
//            NamedNodeMap attr = e.getAttributes();
//            for(int i=0; i < attr.getLength(); i++){
//                Node a = attr.item(i);
//                System.out.println("Name: "+a.getNodeName()+", value: "+a.getNodeValue()+", type: "+a.getNodeType());
//            }
//            System.out.println("fieldType: "+fieldType);
        }
    }

    private static Object estraiValore(Element field, String type){
        String rawValue = getSubElementTextContext(field,NS_SUAP,SUAP_FIELD_VALUE_EL_NAME);
        if(rawValue!=null){
            FieldType ft = FieldType.valueOf(type);
            switch (ft){
                case NUMERIC:
                    String pattern = getSubElementTextContext(field,NS_SUAP,SUAP_FIELD_PATTERN_EL_NAME);
                    if(pattern != null && (
                            pattern.equals(PATTERN_DUE_DECIMALI) ||
                                    pattern.equals(PATTERN_TRE_DECIMALI) ||
                                    pattern.equals(PATTERN_DECIMALI_LIBERI)

                            )){
                        return  Double.parseDouble(rawValue.replace(",", "."));
                        }
                        return Integer.parseInt(rawValue);
                default:
                    return rawValue;
            }
        }
//
//        FieldType ft = FieldType.valueOf(type);
//        switch (ft){
//            case NUMERIC:
//                if(field.getPattern() != null){
//                    switch (PatternUtils.Pattern.valueOf(field.getPattern() )) {
//                        case DUE_DECIMALI:
//                        case DECIMALI_LIBERI:
//                        case TRE_DECIMALI:
//                            toReturn = Double.parseDouble(field.getValue().replace(",", "."));
//                            break;
//                        default:
//                            break;
//                    }
//                }else{
//                    toReturn = Integer.parseInt(field.getValue());
//                }
//                break;
//            default:
//                toReturn = field.getValue();
//        }
        return null;
    }

    enum FieldType{
       INPUT,
        NUMERIC,
        CHECKBOX,
        DATE,
        DATE_RANGE,
        TIME_RANGE,
        RADIO,
        TEXTAREA,
        LIST,
        AUTOCOMPLETED_LIST,
        HIDDEN,
        FILE,
        FILE_FIRMATO_DIGITALE,
        READ_ONLY;
    }



    private static String getSubElementTextContext(Element parent, String namespaceUri, String elementName ){
        NodeList nl = parent.getElementsByTagNameNS(namespaceUri, elementName);
        if(nl.getLength()>0){
            return nl.item(0).getTextContent();
        }
        return null;
    }
}

//
//    public static Object estraiValore(String field) {
//
//    FieldType ft = FieldType.valueOf(field);
//        Object toReturn = null;
//        if( FieldtypeWrapper.getInstance( field.getFieldtype().toString()  ) != null ){
//            switch (FieldtypeWrapper.getInstance( field.getFieldtype().toString()  )) {
//                case NUMERIC:
//                    if(field.getPattern() != null){
//                        switch (PatternUtils.Pattern.valueOf(field.getPattern() )) {
//                            case DUE_DECIMALI:
//                            case DECIMALI_LIBERI:
//                            case TRE_DECIMALI:
//                                toReturn = Double.parseDouble(field.getValue().replace(",", "."));
//                                break;
//                            default:
//                                break;
//                        }
//                    }else{
//                        toReturn = Integer.parseInt(field.getValue());
//                    }
//                    break;
//                default:
//                    toReturn = field.getValue();
//            }
//        }
//        return toReturn;
//    }