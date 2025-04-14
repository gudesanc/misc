package exception.data;

import java.net.URI;

/**
 * Interfaccia che restituisce l'URI per la specifica dell'errore
 */
public interface JEnteErrorType {
	
	URI getType();

	enum JEnteCommonErrorType implements JEnteErrorType {
		GENERIC_ERROR("jente://error.generic"),
		VALIDATION_ERROR("jente://error.validation"),
		REQUEST_CTX_NOT_FOUND("jente//error.noctx"),
		ENTITY_NOT_FOUND_ERROR("jente://error.notfound"),
		CONFIGURATION_ERROR("jente://error.configuration"),
		INVALID_OPERATION_IN_CTX("jente//error.nooperation.inctx"),
		PERMISSION_DENIED("jente://error.forbidden"),
		REMOTE_SYSTEM_UNAVAILABLE("jente://error.unavailable"),
		PAGINATION_ORDERING_NOT_VALID("jente://error.paginationOrderingNotValid");

		private final URI uri;

        JEnteCommonErrorType(String uri) {
            this.uri = URI.create(uri);
        }

        @Override
		public URI getType() {
			return uri;
		}
	}
	
}
