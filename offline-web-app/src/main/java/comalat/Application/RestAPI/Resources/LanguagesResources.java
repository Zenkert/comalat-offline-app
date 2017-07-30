package comalat.Application.RestAPI.Resources;

import comalat.Constants;
import comalat.Application.Domain.ResponseMessage.SuccessMessage;
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
// */comalat/languages/
@Path("languages")
@Produces(MediaType.APPLICATION_JSON)
public class LanguagesResources {

    // return zip file with all languages
    @GET
    public Response getAllLangauge(
            @HeaderParam("serialNo") long serialNo,
            @HeaderParam("foldername") String foldername) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }
        
        if(foldername == null || foldername.isEmpty()){
            foldername = "langs_";
        }
        
        String zipname = foldername + serialNo + Constants.ZIP_FORMAT;

        CompressManager.Compression(Constants.SOURCE_FOLDER, Constants.DOWNLOAD_FOLDER, zipname);
        File file = new File(Paths.get(Constants.DOWNLOAD_FOLDER, zipname).toString());
        
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"Languages.zip\"")
                .header("x-zipfilename", file.getName())
                .header("x-fileformat", Constants.ZIP_FORMAT)
                .build();
    }

    // delete source folder
    // */comalat/languages/
    // Response 200 OK || 404 NOT FOUND
    @DELETE
    public Response deleteAllLangauges() {
        
        if(!FolderManager.deleteAll(Constants.SOURCE_FOLDER)){
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.status(Status.OK).build();
    }

    // get a language
    // */comalat/languages/{lang}
    @GET
    @Path("/{lang}")
    public Response getLanguage(
            @PathParam("lang") String lang,
            @HeaderParam("serialNo") long serialNo) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }
        String zipname = lang + "_" + serialNo + Constants.ZIP_FORMAT;
        String path = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);
        if(path == null){
            throw new DataNotFoundException("Can not find folder/file " + "{" + lang + "}");
        }

        CompressManager.Compression(path, Constants.DOWNLOAD_FOLDER, zipname);
        File file = new File(Paths.get(Constants.DOWNLOAD_FOLDER, zipname).toString());
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + lang + Constants.ZIP_FORMAT + "\"")
                .header("x-zipfilename", file.getName())
                .header("x-fileformat", Constants.ZIP_FORMAT)
                .build();
    }
    
    // delete a language
    // */comalat/languages/{lang}
    // Response 200 OK || 404 NOT FOUND
    @DELETE
    @Path("/{lang}")
    public Response deleteLanguage(
            @PathParam("lang") String lang) {
        
        String path = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);
        if(path == null){
            return Response.status(Status.NOT_FOUND).build();
        }        
        FolderManager.delete(path);
        return Response.status(Status.OK).build();
    }

    // */comalat/languages/upload
    // Response 201 CREATE || 404 NOT FOUND || 404 CONFLICT
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("name") String filename,
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

        if (FolderManager.exist(Constants.SOURCE_FOLDER, filename)) {
            // conflict error the language exist
            throw new ConflictException("Language "+filename+" already exist!");
        }
        filename = filename.concat("_"+serialNo+Constants.ZIP_FORMAT);
        
        FolderManager.saveUploadedFile(in, Constants.UPLOAD_FOLDER, filename);
        CompressManager.Decompression(Constants.UPLOAD_FOLDER, Constants.SOURCE_FOLDER, filename);
        
        SuccessMessage message = new SuccessMessage("Upload "+info.getFileName(), Status.CREATED.getStatusCode());
        return Response.status(Status.CREATED).entity(message).build();
    }
    
    // */comalat/languages/update
    @PUT
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("name") String filename,
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
        String langName = new String(filename);
        filename = filename.replace(" ", "");
        filename = filename.concat("_"+serialNo+Constants.ZIP_FORMAT);
        
        FolderManager.saveUploadedFile(in, Constants.UPLOAD_FOLDER, filename);
        if (FolderManager.getPath(Constants.SOURCE_FOLDER, langName) != null) {
            FolderManager.delete(FolderManager.getPath(Constants.SOURCE_FOLDER, langName));
        }
        CompressManager.Decompression(Constants.UPLOAD_FOLDER, Constants.SOURCE_FOLDER, filename);
        
        SuccessMessage message = new SuccessMessage("Updated "+info.getFileName(), Status.OK.getStatusCode());
        return Response.status(Status.OK).entity(message).build();
    }
    
    /*********** SubResources ***********/
    
    // levels subresource
    // */comalat/languages/{lang}/levels/
    @Path("/{lang}/levels")
    public LevelsResources levelSubresource(){
        return new LevelsResources();
    }
}