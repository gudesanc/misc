package exception.restmapper;

import exception.JEnteExceptionFactory;
import exception.data.JEnteProblemDetail;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import org.eclipse.microprofile.rest.client.ext.ResponseExceptionMapper;

@Provider
public class JEnteResponseExceptionMapper implements ResponseExceptionMapper<RuntimeException> {

    @Override
    public RuntimeException toThrowable(Response response) {
        System.out.println("\n\n\n\nPEPEPEPEPEP STO QUI\n\n\n\n");
        //Proviamo a vedere se è il nostro jsoon di errore altrimenti demandiamo la gestione al prossimo response exception mapper
        try {
            JEnteProblemDetail erroreInterno = response.readEntity(JEnteProblemDetail.class);
            return JEnteExceptionFactory.createException(erroreInterno);
        }catch (Exception e){
            System.out.println("\n\n\n\nPEPEPEPEPEP STO QUI MERD: "+e+"\n\n\n\n");
            //to nothing
            return null;
        }
    }
}
