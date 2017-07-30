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
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException> {

    @Override
    public Response toResponse(DataNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage(), Status.NOT_FOUND.getStatusCode(), "Information");
        return Response.status(Status.NOT_FOUND)
                .entity(errorMessage)
                .build();
    }

}
