package it.gds.misc;

import it.eng.suap.suapschema.*;

public class FieldUtils {

    public static final String CHECKBOX_VALUE = "getcheck";
    public static final String LIST_EMPTY_VALUE = "-1";
    public static final String EXTRA_INFO_LABEL_SUBLIST = "sublistValue";

    /* utilizzato per l'accesso ai metodi da freemarker template */
    private static FieldUtils fieldUtils;
    public static FieldUtils getInstance(){
        if(fieldUtils == null){
            fieldUtils = new FieldUtils();
        }
        return fieldUtils;
    }

    public enum FieldtypeWrapper {

        /*
         * ATTENZIONE: l'aggiunta di un pattern comporta la modifica anche del metodo "Object estraiValore(Field field)"
         */
        INPUT("INPUT", FieldType.INPUT, 200, 10),
        NUMERIC("NUMERICO", FieldType.NUMERIC, 20, 10),
        CHECKBOX("CHECKBOX",FieldType.CHECKBOX),
        DATE("DATA",FieldType.DATE,10,6), //per i campi dati il size e' espresso in em poiche' javascript che imposta il calendario
        DATE_RANGE("INTERVALLO DATE",FieldType.DATE_RANGE,10,6),
        TIME_RANGE("INTERVALLO ORARIO",FieldType.TIME_RANGE,5,4),
        RADIO("RADIO",FieldType.RADIO),
        TEXTAREA("TEXTAREA",FieldType.TEXTAREA, 4000, 65),
        LIST("LISTA",FieldType.LIST),
        AUTOCOMPLETED_LIST("LISTA CON AUTOCOMPLETAMENTO",FieldType.AUTOCOMPLETED_LIST),
        HIDDEN("HIDDEN",FieldType.HIDDEN),
        FILE("FILE",FieldType.FILE),
        FILE_FIRMATO_DIGITALE("FILE FIRMATO DIGITALMENTE",FieldType.FILE_FIRMATO_DIGITALE),
        READ_ONLY("SOLA LETTURA",FieldType.READ_ONLY);

        private String descrizione;
        private FieldType  fieldType;
        private Integer maxLengthDefault = null;
        private Integer sizeDefault = null;

        FieldtypeWrapper(String desc, FieldType fType) {
            descrizione = desc;
            fieldType = fType;
        }

        FieldtypeWrapper(String desc, FieldType fType, Integer maxLengthDefault, Integer sizeDefault) {
            this(desc, fType);
            this.maxLengthDefault = maxLengthDefault;
            this.sizeDefault = sizeDefault;
        }

        public String getDescrizione() {
            return this.descrizione;
        }

        public FieldType getFieldtype() {
            return this.fieldType;
        }

        public Integer getMaxLengthDefault() {
            return maxLengthDefault;
        }

        public Integer getSizeDefault() {
            return sizeDefault;
        }

        public static FieldtypeWrapper getInstance(String codice) {
            if (codice == null) {
                return null;
            }
            for (FieldtypeWrapper s : FieldtypeWrapper.values()) {
                if (s.name().equals(codice)) {
                    return s;
                }
            }
            return null;
        }


    };

    public static String getFieldtypeDescription(String name) {
        FieldtypeWrapper 	found = FieldtypeWrapper.getInstance(name);
        if(found != null){
            return found.getDescrizione();
        }else{
            return "Non Trovato";
        }

    }


    /**
     * ritorna il primo elemento ExtraInfoContent se presente, null altrimenti
     * @param field
     * @return
     */
    public static ExtraInfoContent getFirstExtraInfoContent(Field field){
        if(field.getExtraInfo() != null && field.getExtraInfo().getExtraInfoContent() !=null
                && field.getExtraInfo().getExtraInfoContent().size()>0
                && field.getExtraInfo().getExtraInfoContent().get(0) != null){
            return field.getExtraInfo().getExtraInfoContent().get(0);
        }
        return null;
    }

    /**
     * Ritorna il value di un field sulla base del pattern presente
     * @param field
     * @return
     */
    public static Object estraiValore(Field field) {

        if(field==null || field.getValue()==null || field.getFieldtype()==null){
            return null;
        }
        return switch (field.getFieldtype()){
            case AUTOCOMPLETED_LIST, LIST ->
                    getListValue(field);
            case NUMERIC  ->
                    getNumericValue(field);
            case INPUT, HIDDEN, TEXTAREA, READ_ONLY->
                    field.getValue();
            default ->
                    throw new UnsupportedOperationException("Tipo "+field.getFieldtype()+" non ancora supportato");

        };
    }

    private static Object getNumericValue(Field field){
        if(field.getValue()==null ){
            return null;
        }
        if(field.getPattern()!=null){
            PatternUtils.Pattern pattern = PatternUtils.Pattern.getInstance(field.getPattern());
            switch (pattern){
                case DUE_DECIMALI, TRE_DECIMALI,TRE_DECIMALI_OBBLIGATORI:
                  return  Double.parseDouble(field.getValue().replace(",", "."));
            }
        }

        return Integer.parseInt(field.getValue());

    }

    private static String getListValue(Field field){
        String value = field.getValue();
        if(value==null || field.getAllowedValues()==null || field.getAllowedValues().getAllowedValue()==null){
            return null;
        }
        //ok cerchiamo negli allowed value quello che ha il value selezionato e restitiamo la label
        AllowedValues allowedValues = field.getAllowedValues();
        return  field.getAllowedValues().getAllowedValue()
                .stream()
                .filter(element -> element.getValue().equals(value))
                .map(AllowedValue::getLabel).findFirst().orElseGet(null);
    }

    public enum AdditionalTypeWrapper {
        NO_SUPPORTED("Nessuno",null,0),
        NOTE_SUPPORTED("Note", AdditionalType.TEXTAREA,1),
        FILE_SUPPORTED("File",AdditionalType.FILE,2),
        FILE_FIRMATO_SUPPORTED("File Firmato",AdditionalType.FILE_FIRMATO_DIGITALE,3),
        INPUT_SUPPORTED("Input",AdditionalType.INPUT,4),
        DATE_SUPPORTED("Data",AdditionalType.DATE,5);

        private String descrizione;
        private int codice;
        private AdditionalType additionalType;

        public String getDescrizione() {
            return descrizione;
        }

        public int getCodice() {
            return codice;
        }

        public AdditionalType getAdditionalType() {
            return additionalType;
        }

        AdditionalTypeWrapper(String desc, AdditionalType aType, int code) {
            descrizione = desc;
            additionalType = aType;
            codice = code;
        }

        public static AdditionalTypeWrapper getInstanceByAdditionlType(String additionalType) {
            for (AdditionalTypeWrapper s : AdditionalTypeWrapper.values()) {
                //il NO_SUPPORTED e' null quindi devo prevedere il caso
                if(s.getAdditionalType()== null) continue;
                if (s.getAdditionalType().toString().equals(additionalType)) {
                    return s;
                }
            }
            return NO_SUPPORTED;
        }

        public static AdditionalTypeWrapper getInstanceByAdditionlCodice(int codice) {
            for (AdditionalTypeWrapper s : AdditionalTypeWrapper.values()) {
                if (s.getCodice() ==codice) {
                    return s;
                }
            }
            return null;
        }

    }

}
