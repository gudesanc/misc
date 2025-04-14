package exception;

import exception.data.JEnteCommonErrors;

public class JEnteConfigurationException extends JEnteRuntimeException{
    public JEnteConfigurationException(String detailMessage) {
        super(JEnteCommonErrors.JENTE_CONFIGURATION_ERROR.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_CONFIGURATION_ERROR.getTitle(),
                JEnteCommonErrors.JENTE_CONFIGURATION_ERROR.getErrorCode(),null,
                JEnteCommonErrors.JENTE_CONFIGURATION_ERROR.getErrorType());
    }
}
