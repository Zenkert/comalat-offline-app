package comalat.Application.Exception;

import comalat.Application.Domain.ResponseMessage.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author SyleSakis
 */
@Provider
public class ServerProcedureExceptionMapper implements ExceptionMapper<ServerProcedureException>{

    @Override
    public Response toResponse(ServerProcedureException ex) {
        
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Status.SERVICE_UNAVAILABLE.getStatusCode(), "Please inform us for this error");
        return Response.status(Status.SERVICE_UNAVAILABLE)
                .entity(errorMessage)
                .build();
    }    
}
