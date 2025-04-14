package exception;

import exception.data.JEnteCommonErrors;

/**
 * Eccezione utilizzata per indicare che la specifica operazione richiesta non e' disponibile
 * nel contesto attuale.
 * Un esempio e' provare a caricare una proprieta' di configurazione utilizzando una tabella
 * che non e' presente per la procedura alla quale fa riferimento il cotesto
 */
public class JEnteUnsupportedOperationInContextException extends JEnteRuntimeException{
    public JEnteUnsupportedOperationInContextException(String detailMessage) {
        super(JEnteCommonErrors.JENTE_UNSUPPORTED_OPERATION_IN_CTX.getStatus().getStatusCode(),
                detailMessage,
                JEnteCommonErrors.JENTE_UNSUPPORTED_OPERATION_IN_CTX.getTitle(),
                JEnteCommonErrors.JENTE_UNSUPPORTED_OPERATION_IN_CTX.getErrorCode(),null,
                JEnteCommonErrors.JENTE_UNSUPPORTED_OPERATION_IN_CTX.getErrorType());
    }
}
