package comalat.Application.RestAPI.Resources;

import comalat.Application.Domain.ResponseMessage.ErrorMessage;
import comalat.Application.Domain.ResponseMessage.SuccessMessage;
import comalat.Application.Domain.ResponseMessage.UserForm;
import comalat.Services.FileServices.AccessData;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author SyleSakis
 */
@Path("login")
public class UserResources {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @FormDataParam("username") String username,
            @FormDataParam("password") String password) {

        if (AccessData.compareData(username, password)) {
            UserForm user = new UserForm();
            user.setUsername(username);
            user.setPassword(password);
            user.setFullname(AccessData.getFullname());
            return Response.ok().entity(user).build();
        }

        ErrorMessage em = new ErrorMessage();
        em.setMessage("Invalid Username or Password!");
        em.setCode(Status.FORBIDDEN.getStatusCode());
        em.setDocumentation("Contact us for more information");
        return Response.status(Status.FORBIDDEN).entity(em).build();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response edit(
            @FormDataParam("username") String username,
            @FormDataParam("password") String password,
            @FormDataParam("fullname") String fullname) {

        AccessData.updateAccessFile(username, password, fullname);
        SuccessMessage sm = new SuccessMessage();
        sm.setMessage("Updated successfully!!");
        sm.setCode(Status.OK.getStatusCode());
        sm.setDocumantation("");
        return Response.ok().entity(sm).build();

    }
}
