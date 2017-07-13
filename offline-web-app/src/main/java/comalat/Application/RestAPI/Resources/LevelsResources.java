package comalat.Application.RestAPI.Resources;

import comalat.Constants;
import comalat.Application.Domain.SuccessMessage;
import comalat.Application.Exception.ConflictException;
import comalat.Application.Exception.DataNotFoundException;
import comalat.Application.Exception.InvalidInputException;
import comalat.Services.FolderServices.CompressManager;
import comalat.Services.FolderServices.FolderManager;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author SyleSakis
 */
// */comalat/languages/{lang}/levels/
@Produces(MediaType.APPLICATION_JSON)
public class LevelsResources {

    @GET
    public Response get() {
        SuccessMessage message = new SuccessMessage("LEVELS", Status.OK.getStatusCode());
        return Response.status(Status.OK).entity(message).build();
    }

    // get education level from a language 
    // */comalat/languages/{lang}/levels/{lvl}
    @GET
    @Path("/{lvl}")
    public Response getEducationLevel(
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl,
            @HeaderParam("serialNo") long serialNo) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }
        String tmpname = lang + "_" + lvl;
        String zipname = tmpname + "_" + serialNo + Constants.ZIP_FORMAT;

        String path = FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl);
        if (path == null) {
            throw new DataNotFoundException("Can not find folder/file " + "{" + lvl + "} at folder {" + lang + "}");
        }

        CompressManager.Compression(path, Constants.DESTINATION_DOWNLOAD_FOLDER, zipname);
        File file = new File(Paths.get(Constants.DESTINATION_DOWNLOAD_FOLDER, zipname).toString());
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + tmpname + Constants.ZIP_FORMAT + "\"")
                .build();
    }

    // delete education level from a language
    // */comalat/languages/{lang}/levels/{lvl}
    @DELETE
    @Path("/{lvl}")
    public Response deleteEducationLevel(
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl) {

        String path = FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl);
        if (path == null) {
            throw new DataNotFoundException("Can not find folder/file " + "{" + lvl + "} at folder {" + lang + "}");
        }

        FolderManager.delete(path);

        SuccessMessage message = new SuccessMessage("Delete " + lvl + " at folder " + lang, Status.OK.getStatusCode());
        return Response.status(Status.OK).entity(message).build();
    }

    // upload education level from a language
    // */comalat/languages/{lang}/levels/upload
    // Response 201 CREATE || 404 NOT FOUND || 404 CONFLICT
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("lvlName") String filename,
            @PathParam("lang") String lang,
            @HeaderParam("serialNo") long serialNo) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }

        if (in == null || !info.getFileName().endsWith(Constants.ZIP_FORMAT)) {
            throw new InvalidInputException("Please select zip format file");
        }

        if (filename == null || filename.replace(" ", "").isEmpty()) {
            // invalid filename input
            // if is null get name from info
            throw new InvalidInputException("Please input file name");
        }
        filename = filename.replace(" ", "");

        String source = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);
        if (FolderManager.exist(source, filename)) {
            // conflict error the education level exist
            throw new ConflictException("Edication level "+filename+" already exist!");
        }
        filename = filename.concat("_" + serialNo + Constants.ZIP_FORMAT);

        FolderManager.saveUploadedFile(in, Constants.DESTINATION_UPLOAD_FOLDER, filename);
        CompressManager.Decompression(Constants.DESTINATION_UPLOAD_FOLDER, source, filename);

        SuccessMessage message = new SuccessMessage("Upload " + info.getFileName(), Status.CREATED.getStatusCode());
        return Response.status(Status.CREATED).entity(message).build();
    }

    // update education level from a language
    // */comalat/languages/{lang}/levels/update
    @PUT
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("lvlName") String filename,
            @PathParam("lang") String lang,
            @HeaderParam("serialNo") long serialNo) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }

        if (in == null || !info.getFileName().endsWith(Constants.ZIP_FORMAT)) {
            throw new InvalidInputException("Please select zip format file");
        }

        if (filename == null || filename.replace(" ", "").isEmpty()) {
            throw new InvalidInputException("Please input file name");
        }
        String lvlName = new String(filename);
        filename = filename.replace(" ", "");

        String source = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);
        filename = filename.concat("_" + serialNo + Constants.ZIP_FORMAT);

        FolderManager.saveUploadedFile(in, Constants.DESTINATION_UPLOAD_FOLDER, filename);
        if (FolderManager.getPath(source, lvlName) != null) {
            FolderManager.delete(FolderManager.getPath(source, lvlName));
        }
        CompressManager.Decompression(Constants.DESTINATION_UPLOAD_FOLDER, source, filename);

        SuccessMessage message = new SuccessMessage("Updated " + info.getFileName(), Status.CREATED.getStatusCode());
        return Response.status(Status.OK).entity(message).build();
    }
    
    /*********** SubResources ***********/
    
    // units subresource
    // */comalat/languages/{lang}/levels/{lvl}/courses
    @Path("/{lvl}/courses")
    public CoursesResources UnitSubresource(){
        return new CoursesResources();
    }
}
