package comalat.Application.Exception;

import comalat.Application.Domain.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author SyleSakis
 */
@Provider
public class InvalidInputExceptionMapper implements ExceptionMapper<InvalidInputException>{

    @Override
    public Response toResponse(InvalidInputException ex) {
       ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Status.NOT_ACCEPTABLE.getStatusCode(), "url_link");
        return Response.status(Status.NOT_ACCEPTABLE)
                .entity(errorMessage)
                .build(); 
    }
    
}
