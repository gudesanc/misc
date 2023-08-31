package org.gds.misc.xdocreport.converter.odt;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;

import java.io.*;

public class Converter {
    public static void main(String[] args) throws Exception {
        //
        //
        // PROTOCOLLO  19577
        //
        //
        //
// 1) Create options ODT 2 PDF to select well converter form the registry
        Options options = Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);

// 2) Get the converter from the registry
        IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

// 3) Convert ODT 2 PDF
        InputStream in= new FileInputStream(new File("/tmp/Bozza.odt"));
        OutputStream out = new FileOutputStream(new File("/tmp/ODTHelloWord2PDF_odt_originale.pdf"));
        converter.convert(in, out, options);
    }
}
