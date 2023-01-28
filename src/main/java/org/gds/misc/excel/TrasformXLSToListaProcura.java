package org.gds.misc.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TrasformXLSToListaProcura {

    public static void main(String[] args) throws Exception {
        TrasformXLSToListaProcura trasformer = new TrasformXLSToListaProcura();
        List<ProcuraInfoData> records = trasformer.leggiFileExcel("/home/gustavo/dev/github/misc/src/main/resources/poi/QUESTURA_104.xlsx");
        System.out.println("Letti "+records.size()+" elementi dal file excel");
        trasformer.toFileProcura(records);
        System.out.println("Processamento concluso");
    }
    private List<ProcuraInfoData> leggiFileExcel(String path) throws Exception {
        List<ProcuraInfoData> records = new ArrayList<>();
        try(FileInputStream excelFile = new FileInputStream(path)){
            Workbook workbook = WorkbookFactory.create(excelFile);
            // utilizzare workbook per accedere alle informazioni del file Excel
            // esempio:
            System.out.println("Workbook has " + workbook.getNumberOfSheets() + " Sheets : ");
            workbook.forEach(sheet -> {
                System.out.println("=> " + sheet.getSheetName());
            });
            System.out.println("Si utilizza il primo sheet...");
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row: sheet){
                records.add(new ProcuraInfoData(row));
            }
        }
        return records;
    }

    private void toFileProcura(List<ProcuraInfoData> items) throws Exception {
        DateFormat sdfProcura = new SimpleDateFormat("YYYY-MM-dd");
        StringBuilder content = new StringBuilder();
        for (ProcuraInfoData item: items) {
                content.append(generateToken(item.getCognome(),72));
                content.append(generateToken(item.getNome(),72));
                if (item.getCodiceBelfioreComuneItalianoNascita()!=null) {
                    content.append(generateToken(item.getCodiceBelfioreComuneItalianoNascita(), 4));
                } else {
                    content.append("    ");
                }
                if (item.getComuneEsteroNascita()!=null) {
                    content.append(generateToken(item.getComuneEsteroNascita(),60));
                } else {
                    content.append(StringUtils.rightPad("",60));
                }

                if (item.getDataNascita()!=null) {
                    content.append(generateToken(sdfProcura.format(item.getDataNascita()),10));
                } else {
                    content.append(StringUtils.rightPad("",10));
                }

                if (item.getCodiceBelfioreNazioneEsteraNascita()!=null) {
                    content.append(generateToken(item.getCodiceBelfioreNazioneEsteraNascita(),4));
                } else {
                    content.append("Z000");
                }
                content.append(StringUtils.rightPad("",72)); // Paternita'
                content.append(item.getSesso().name());
                content.append(item.getCodiceFiscale());
                content.append(StringUtils.rightPad("",7)); //Boh
                content.append("\r\n"); // Newline

        }
        System.out.println("Numero record processati: "+items.size());
        String nome="/tmp/PROCURA_"+System.currentTimeMillis()+".txt";
        writeToFile(nome,content);
    }

    private String generateToken(String src, int maxSize) {
        return StringUtils.rightPad(StringUtils.abbreviate(src != null ? src: "" , maxSize), maxSize);
    }
    private void writeToFile(String fileName,StringBuilder source) throws Exception{
        File f = new File(fileName);
        try( FileWriter fw = new FileWriter(f);){
            fw.write(source.toString());
            fw.flush();
        }
        System.out.println("File salvato in: "+f.getAbsolutePath());
    }

}
class ProcuraInfoData {
    private String codiceFiscale;
    private String nome;
    private String cognome;
    private String codiceBelfioreComuneItalianoNascita;
    private String comuneEsteroNascita;
    private Date dataNascita;
    private String codiceBelfioreNazioneEsteraNascita;
    private Sesso sesso;

    ProcuraInfoData(Row row) throws Exception {
        //Se riga da questura:
        //  -0 Cognome
        //  -1 Nome
        //  -2 BelfioreComuneNascitaItalia
        //  -3 DescrizioneComuneNascitaItalia
        //  -4 ComuneEsteroNascita
        //  -5 DataNascita -> dd/MM/yyyy
        //  -6 BeflrioreEsteroNascita
        //  -7 Unkwnows
        //  -8 SESSO
        //  -9 Codice Fiscale
        //
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.cognome = getCellValue(row,0);
        this.nome = getCellValue(row,01);
        this.codiceBelfioreComuneItalianoNascita = getCellValue(row,2);
        this.comuneEsteroNascita = getCellValue(row,4);
        this.dataNascita = getCellValue(row,5)!=null?sdf.parse(getCellValue(row,5)):null;
        this.codiceBelfioreNazioneEsteraNascita = getCellValue(row,6);
        this.sesso = getCellValue(row,8)!=null?Sesso.valueOf(getCellValue(row,8)):null;
        this.codiceFiscale = getCellValue(row,9);
    }


    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodiceBelfioreComuneItalianoNascita() {
        return codiceBelfioreComuneItalianoNascita;
    }

    public String getComuneEsteroNascita() {
        return comuneEsteroNascita;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public String getCodiceBelfioreNazioneEsteraNascita() {
        return codiceBelfioreNazioneEsteraNascita;
    }

    public Sesso getSesso() {
        return sesso;
    }
    public enum Sesso implements Serializable {

        M,
        F;
        public static final long serialVersionUID = 0L;
    }

    private String getCellValue(Row row, int cellNum){
        if(row.getCell(cellNum)==null){
            return null;
        }
        return row.getCell(cellNum).getStringCellValue();
    }

}