package exception;

import exception.data.JEnteCommonErrors;

public class JEnteRemoteServiceUnavailableException extends JEnteRuntimeException{
    public JEnteRemoteServiceUnavailableException(String detailMessage) {
        this(detailMessage,null,null);
    }
    public JEnteRemoteServiceUnavailableException(String detailMessage, String serviceName) {
        this(detailMessage,serviceName,null);
    }
    public JEnteRemoteServiceUnavailableException(String detailMessage, String serviceName, String endpoint) {
        super(JEnteCommonErrors.JENTE_REMOTE_SERVICE_UNAVAILABLE.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_REMOTE_SERVICE_UNAVAILABLE.getTitle(),
                JEnteCommonErrors.JENTE_REMOTE_SERVICE_UNAVAILABLE.getErrorCode(),null,
                JEnteCommonErrors.JENTE_REMOTE_SERVICE_UNAVAILABLE.getErrorType());
        if(serviceName!=null){
            addDetail("service",serviceName);
        }
        if(endpoint!=null){
            addDetail("endpoint",endpoint);
        }
    }
}
