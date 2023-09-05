package org.gds.misc.drools.org.gds.misc.drools.support;

public interface SchemaConstants {
    String NS_MODELLO_UNICO="http://www.eng.it/suap/XMLModelloUnico";
    String NS_SUAP = "http://www.eng.it/suap/suapschema";

    String SUAP_FIELD_TYPE_ATTR_NAME="fieldtype";

    String SUAP_FIELD_VALUE_EL_NAME="value";
    String SUAP_FIELD_PATTERN_EL_NAME="pattern";


    String PATTERN_DUE_DECIMALI="DUE_DECIMALI";
    String PATTERN_DECIMALI_LIBERI="DECIMALI_LIBERI";
    String PATTERN_TRE_DECIMALI="TRE_DECIMALI" ;

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

}
