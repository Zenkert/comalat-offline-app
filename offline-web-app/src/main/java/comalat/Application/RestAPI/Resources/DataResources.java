package comalat.Application.RestAPI.Resources;

import comalat.Constants;
import comalat.Application.Domain.Lessons;
import comalat.Application.Exception.DataNotFoundException;
import comalat.Services.FolderServices.FolderManager;

import java.nio.file.Paths;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author SyleSakis
 */
// */comalat/data
@Path("/data")
public class DataResources {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getData(){
        Lessons lessons = new Lessons();
        lessons = lessons.readFromFolder(Constants.SOURCE_FOLDER);
        if(lessons == null){
            throw new DataNotFoundException("Data not found at folder "+ Constants.SOURCE_FOLDER);
        }
        return Response.ok().entity(lessons).build();
    }
    
    @DELETE
    @Path("/{zipfilename}")
    public Response deleteDownLoadedZip(@PathParam("zipfilename") String zipFileName){
        if(zipFileName != null){
            String source = Paths.get(Constants.DOWNLOAD_FOLDER, zipFileName).toString();
            FolderManager.delete(source);
            return Response.status(Status.NO_CONTENT).build();
        }
        
        return Response.status(Status.NOT_MODIFIED).build();
    } 
}
