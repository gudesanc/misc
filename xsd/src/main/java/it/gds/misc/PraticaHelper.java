package it.gds.misc;

import it.eng.suap.suapschema.Field;
import it.eng.suap.xmlmodellounico.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PraticaHelper {
    public static void main(String[] args) throws Exception{
        System.out.println("Ciao");
        new PraticaHelper("xml/a.xml");
    }

    PraticaHelper(String fileName) throws Exception{
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
        ModelloUnico modello = (ModelloUnico)ctx.createUnmarshaller().unmarshal(is);
        System.out.println(modello);
        List<Dichiarazione> dichiarazioni = findDichiarazioni(modello, "intAttivitaSportiveOS", "dicAttivitaSportiveOS");
        dichiarazioni.forEach(d-> System.out.println(d.getDescrizione()));
    }

    public List<Dichiarazione> findDichiarazioni(ModelloUnico modello, String codiceIntervento, String codiceDichiarazione){
        return modello.getProcedimento().stream()
                .map(Procedimento::getIntervento).flatMap(listInterventi -> listInterventi.stream())
                .filter(intervento -> intervento.getCodice().equals(codiceIntervento))
                .map(Intervento::getDichiarazione).flatMap(listDichiarazioni -> listDichiarazioni.stream())
                .filter(dichiarazione -> dichiarazione.getCodice().equals(codiceDichiarazione))
                .collect(Collectors.toList());
    }

//
//    private String readFieldValue(Field field){
//        field.getFieldtype();
//    }

    private static final JAXBContext ctx;

    static {
        try {
            ctx = JAXBContext.newInstance(ModelloUnico.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Errore nell'inizializzazione del contesto JAXB "+e);
        }
    }
}
class InfoAttivita{
    private String attivita;
    private String descrzione;
    private String costo;

    public String getAttivita() {
        return attivita;
    }

    public String getDescrzione() {
        return descrzione;
    }

    public String getCosto() {
        return costo;
    }

    public InfoAttivita(String attivita, String descrzione, String costo) {
        this.attivita = attivita;
        this.descrzione = descrzione;
        this.costo = costo;
    }
}