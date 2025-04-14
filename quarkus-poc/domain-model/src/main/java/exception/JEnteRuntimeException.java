package exception;

import exception.data.JEnteErrorDetail;
import exception.data.JEnteErrorType;
import exception.data.JEnteExceptionInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Eccezione generica unchecked
 */
public class JEnteRuntimeException extends RuntimeException implements JEnteExceptionInterface {

	private static final long serialVersionUID = -633285195355864374L;
	
	/**
	 * Codice di stato HTTP
	 */
	private int status;
	
	/**
	 * Messaggio specifico dell'errore
	 */
	private String errorMessage;
	
	/**
	 * Titolo generico dell'errore
	 */
	private String title;
	
	/**
	 * Codice di errore
	 */
	private String errorCode;
	
	/**
	 * Lista dei dettagli di errori
	 * @see JEnteErrorDetail
	 */
	private List<JEnteErrorDetail> details;
	
	/**
	 * Tipologia dell'errore
	 * @see JEnteErrorType
	 */
	private JEnteErrorType jEnteErrorType;
	
	public JEnteRuntimeException(int status, String errorMessage) {
		this(status, errorMessage, null, null, null, null);
	}
	
	public JEnteRuntimeException(int status, String errorMessage, String title) {
		this(status, errorMessage, title, null, null, null);
	}
	
	public JEnteRuntimeException(int status, String errorMessage, String title, String errorCode) {
		this(status, errorMessage, title, errorCode, null, null);
	}

	public JEnteRuntimeException(int status, String errorMessage, JEnteErrorType jEnteErrorType) {
		this(status, errorMessage, null, null, null, jEnteErrorType);
	}

	public JEnteRuntimeException(int status, String errorMessage, String title, String errorCode, List<JEnteErrorDetail> details) {
		this(status, errorMessage, title, errorCode, details, null);
	}
	
	public JEnteRuntimeException(int status, String errorMessage, String title, String errorCode, JEnteErrorType jEnteErrorType) {
		this(status, errorMessage, title, errorCode, null, jEnteErrorType);
	}
	
	public JEnteRuntimeException(int status, String errorMessage, String title, String errorCode, List<JEnteErrorDetail> details, JEnteErrorType jEnteErrorType) {
		super(errorMessage);
		this.status = status;
		this.errorMessage = errorMessage;
		this.title = title;
		this.errorCode = errorCode;
		this.details = details;
		if(jEnteErrorType == null) {
			this.jEnteErrorType = JEnteErrorType.JEnteCommonErrorType.GENERIC_ERROR;
		}
		else {
			this.jEnteErrorType = jEnteErrorType;
		}
	}

	@Override
	public int getStatus() {
		return status;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getErrorCode() {
		return errorCode;
	}

	@Override
	public List<JEnteErrorDetail> getDetails() {
		return details;
	}

	public JEnteRuntimeException addDetail(String key, String value) {
		if(key!=null && value!=null) {
			addDetail(new JEnteErrorDetail(key, value));
		}
		return this;
	}

	public JEnteRuntimeException addDetail(JEnteErrorDetail errorDetail) {
		if(this.details == null) {
			this.details = new ArrayList<>();
		}
		this.details.add(errorDetail);
		return this;
	}
	public JEnteRuntimeException addDetails(Collection<JEnteErrorDetail> errorDetails) {
		if(errorDetails!=null){
			errorDetails.forEach(this::addDetail);
		}
		return this;
	}

	@Override
	public JEnteErrorType getJEnteErrorType() {
		return jEnteErrorType;
	}
	@Override
	public String getMessage() {
		return "JEnteException{" +
				"status=" + status +
				", errorMessage='" + errorMessage + '\'' +
				", title='" + title + '\'' +
				", errorCode='" + errorCode + '\'' +
				", details=" + details +
				", jEnteErrorType=" + jEnteErrorType +
				'}';
	}
}
