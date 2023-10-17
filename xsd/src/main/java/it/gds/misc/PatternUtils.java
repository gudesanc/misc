package it.gds.misc;


import it.eng.suap.suapschema.FieldType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatternUtils {

	private static Map<FieldUtils.FieldtypeWrapper, List<Pattern>> allPatternField = new HashMap<FieldUtils.FieldtypeWrapper, List<Pattern>>();

	static {
   	for(FieldUtils.FieldtypeWrapper type : FieldUtils.FieldtypeWrapper.values()){
   		List<Pattern> listPattern = new ArrayList<Pattern>();
   		//inserisco i pattern relativi
   		for(Pattern pattern : Pattern.values()){
      		if(pattern.getFieldtype() == type.getFieldtype()){
      			listPattern.add(pattern);
      		}
      	}
   		allPatternField.put(type, listPattern);
   	}
	}

	public static Map<FieldUtils.FieldtypeWrapper, List<Pattern>> getPatternMap() {
      return allPatternField;
   }

	public enum Pattern {
		/*
		 * ATTENZIONE: l'aggiunta di un pattern comporta la modifica anche del metodo "Object estraiValore(Field field)" in FieldUtils.class
		 */
		DUE_DECIMALI(FieldType.NUMERIC,"^\\-?\\d+(\\,\\d{1,2})?$","Max Due cifre decimali","(es. 123,45)", "massimo due cifre decimali (es. 123,45)"),
		TRE_DECIMALI(FieldType.NUMERIC,"^\\-?\\d+(\\,\\d{1,3})?$","Max Tre cifre decimali","(es. 123,456)", "massimo tre cifre decimali (es. 123,456)"),
		TRE_DECIMALI_OBBLIGATORI(FieldType.NUMERIC,"^\\-?\\d+(\\,\\d{3})?$","Tre cifre decimali Obbligatorie","(es. 123,456)", "Tre cifre decimali (es. 123,456)"),
		DECIMALI_LIBERI(FieldType.NUMERIC,"^\\-?\\d+(\\,\\d*)?$","Decimali Liberi","(es. 123,45)");
		private FieldType fieldtype;
		private String valore;
		private String descrizione;
		private String note;
		private String errore;


		Pattern(FieldType fieldtype, String valore, String descrizione, String note) {
			this.fieldtype = fieldtype;
			this.valore = valore;
			this.descrizione = descrizione;
			this.note=note;

		}

		Pattern(FieldType fieldtype, String valore, String descrizione, String note, String errore) {
			this.fieldtype = fieldtype;
			this.valore = valore;
			this.descrizione = descrizione;
			this.note=note;
			this.errore = errore;
		}

		public String getKey() {
			return name();
		}

		public String getDescrizione() {
			return this.descrizione;
		}

		public FieldType getFieldtype() {
			return this.fieldtype;
		}

		public String getValore() {
			return valore;
		}

		public String getNote() {
			return this.note;
		}

		public String getErrore() {
			return errore;
		}



		public static Pattern getInstance(String codice) {
			if (codice == null) {
				return null;
			}
			for (Pattern s : Pattern.values()) {
				if (s.name().equals(codice)) {
					return s;
				}
			}
			return null;
		}

	};


}
