package org.gds.misc.xpath;

import java.io.InputStream;

public class XPathTesterMain {

    public static void main(String[] args) {
        InputStream is = XPathTesterMain.class.getClassLoader().getResourceAsStream("a-nos.xml");
        XMLDocumentInfo xmlDocumentInfo = new XMLDocumentInfo(is);
        String xpath = "/";
        System.out.println(xmlDocumentInfo.getNode(xpath));
        System.out.println(xmlDocumentInfo.getNodeList(xpath));
        System.out.println(xmlDocumentInfo.getNodeList(xpath).getLength());
        System.out.println(xmlDocumentInfo.getNodeList(xpath).item(0));
    }
}
