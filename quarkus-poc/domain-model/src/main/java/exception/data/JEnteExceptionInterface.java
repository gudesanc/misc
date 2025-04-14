package exception.data;

import exception.data.JEnteErrorDetail;

import java.util.List;

/**
 * Interfaccia condivisa dalle eccezioni gestite e non gestite.
 * 
 * Le costanti vengono utilizzate per dare un nome alle proprietà inserite nel ProblemDetail gestito nel Controller Advice
 */
public interface JEnteExceptionInterface {
	

	int getStatus();
	
	String getTitle();
	
	String getErrorMessage();
	
	String getErrorCode();
	
	List<JEnteErrorDetail> getDetails();
	
	JEnteErrorType getJEnteErrorType();

}
