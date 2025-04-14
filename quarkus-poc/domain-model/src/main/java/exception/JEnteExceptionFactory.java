package exception;

import exception.JEnteConfigurationException;
import exception.JEntePermissionDeniedException;
import exception.JEnteRequestCtxNotFoundException;
import exception.JEnteUnsupportedOperationInContextException;
import exception.data.JEnteCommonErrors;
import exception.data.JEnteErrorType;
import exception.data.JEnteProblemDetail;

import java.net.URI;
import java.util.Optional;

/**
 * Support class to create a JEnteException starting from
 * a JEnteProblemDetail
 * ...it tries to create the most appropriate class.. but if none of the known ones fits it use
 * a generic JEnteRuntimeException
 */
public class JEnteExceptionFactory {

    public static JEnteRuntimeException createException(JEnteProblemDetail e){
        //Ok vediamo se è un errore noto a questo progetto:
        Optional<JEnteCommonErrors> commonError = JEnteCommonErrors.getRelatedJEnteCommonErrors(e);
        if(commonError.isPresent()){
            return switch (commonError.get()){
                case JENTE_REQUEST_CTX_NOT_FOUND -> new JEnteRequestCtxNotFoundException(e.getDetail()).addDetails(e.getDetails());
                case JENTE_PERMISSION_DENIED -> new JEntePermissionDeniedException(e.getDetail()).addDetails(e.getDetails());
                case JENTE_ENTITY_NOT_FOUND -> new JEnteEntityNotFoundException(e.getDetail()).addDetails(e.getDetails());
                case JENTE_CONFIGURATION_ERROR -> new JEnteConfigurationException(e.getDetail()).addDetails(e.getDetails());
                case JENTE_VALIDATION_ERROR -> new JEnteRuntimeException(e.getStatus(),e.getDetail(),e.getTitle(), e.getErrorCode(),e.getDetails(),commonError.get().getErrorType());
                case JENTE_REMOTE_SERVICE_UNAVAILABLE -> new JEnteRemoteServiceUnavailableException(e.getDetail()).addDetails(e.getDetails());
                case JENTE_UNSUPPORTED_OPERATION_IN_CTX -> new JEnteUnsupportedOperationInContextException(e.getDetail());
            };
        }
        else {
            JEnteErrorType errorType = getErrorType(e.getType());
         return new JEnteRuntimeException(e.getStatus(),e.getDetail(),e.getTitle(), e.getErrorCode(),e.getDetails(),errorType);
        }
    }

    private static JEnteErrorType getErrorType(URI u){
        if(u==null){
            return JEnteErrorType.JEnteCommonErrorType.GENERIC_ERROR;
        }
        for(JEnteErrorType.JEnteCommonErrorType commonErrorType: JEnteErrorType.JEnteCommonErrorType.values()){
            if(commonErrorType.getType().equals(u)){
                return commonErrorType;
            }
        }
        //Niente da fare non son quelli comuni restituiamo un'error type generico
        return new JEnteErrorType() {
            @Override
            public URI getType() {
                return u;
            }
        };
    }

    public static <E extends Enum<E>> Optional<java.lang.Enum<E>> getValoreAsEnum(String quelloCheTePAre, Class<E> enumClazz) throws JEnteConfigurationException, JEnteUnsupportedOperationInContextException{
        E[] enumValues = enumClazz.getEnumConstants();
        if(enumValues==null){
            return Optional.empty();
        }
        for(E enumVal : enumValues){
            if(enumVal.name().equals(quelloCheTePAre)){
                return Optional.of(enumVal);
            }
        }
        return Optional.empty();
    }

}
