package comalat.Application.RestAPI.Resources;

import comalat.Constants;
import comalat.Application.Domain.ResponseMessage.ErrorMessage;
import comalat.Application.Domain.ResponseMessage.SuccessMessage;
import comalat.Application.Exception.DataNotFoundException;
import comalat.Application.Exception.ConflictException;
import comalat.Application.Exception.InvalidInputException;
import comalat.Services.FolderServices.FolderManager;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
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
// */comalat/languages/{lang}/levels/{lvl}/courses/{coursesXY}/units
@Produces(MediaType.APPLICATION_JSON)
public class UnitResources {

    @GET
    public Response get() {
        SuccessMessage message = new SuccessMessage("Units", Status.OK.getStatusCode(), null);
        return Response.status(Status.OK).entity(message).build();
    }

    // get pdf file from a language/education_level/courses 
    // */comalat/languages/{lang}/levels/{lvl}/courses/{coursesXY}/units/{unit}
    @GET
    @Path("/{unit}")
    public Response getUnitsXY(
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl,
            @PathParam("coursesXY") String coursesXY,
            @PathParam("unit") String unit) {

        String source = FolderManager.getPath(FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl), coursesXY);
        source = FolderManager.getPath(source, unit);
        String filename = FolderManager.getFileName(source);
        if (filename == null) {
            throw new DataNotFoundException("Can not find pdf file at folder {" + lang + "/" + lvl + "/" + coursesXY + "/" + unit + "}");
        }

        File file = new File(Paths.get(source, filename).toString());
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + filename + "\"")
                .header("x-pdffilename", file.getName())
                .header("x-fileformat", Constants.PDF_FORMAT)
                .build();
    }

    // delete pdf file from language/education_level/courses
    // */comalat/languages/{lang}/levels/{lvl}/courses/{coursesXY}/units/{unit}
    // Response 200 OK || 404 NOT FOUND
    @DELETE
    @Path("/{unit}")
    public Response deletePDFFile(
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl,
            @PathParam("coursesXY") String coursesXY,
            @PathParam("unit") String unit) {

        String source = FolderManager.getPath(FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl), coursesXY);
        source = FolderManager.getPath(source, unit);

        if (source == null) {
            ErrorMessage em = new ErrorMessage(unit + " does not exist at folder" + coursesXY, Status.NOT_FOUND.getStatusCode(), null);
            return Response.status(Status.NOT_FOUND).entity(em).build();
        }
        FolderManager.delete(source);
        SuccessMessage sm = new SuccessMessage(unit + " successfully deleted", Status.OK.getStatusCode(), null);
        return Response.status(Status.OK).entity(sm).build();
    }

    // upload pdf file to language/education_level/courses
    // */comalat/languages/{lang}/levels/{lvl}/courses/{coursesXY}/units/upload
    // Response 201 CREATE || 404 NOT FOUND || 404 CONFLICT
    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("name") String unit,
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl,
            @PathParam("coursesXY") String coursesXY) {

        if (in == null || !info.getFileName().endsWith(Constants.PDF_FORMAT)) {
            throw new InvalidInputException("Please select pdf format file");
        }

        if (unit == null || unit.replace(" ", "").isEmpty()) {
            // invalid filename input
            // if is null get name from info
            throw new InvalidInputException("Please input file name");
        }

        String source = FolderManager.getPath(FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl), coursesXY);
        if (FolderManager.getPath(source, unit) != null) {
            if (FolderManager.getFileName(FolderManager.getPath(source, unit)) != null
                    && FolderManager.getFileName(FolderManager.getPath(source, unit))
                            .equalsIgnoreCase(info.getFileName())) {
                throw new ConflictException("PDF file " + info.getFileName() + " at folder " + unit + " already exist!");
            }
        }

        FolderManager.saveUploadedFile(in, Paths.get(source, unit).toString(), info.getFileName());

        SuccessMessage message = new SuccessMessage("Upload " + info.getFileName(), Status.CREATED.getStatusCode(), null);
        return Response.status(Status.CREATED).entity(message).build();
    }

    // update pdf file to language/education_level/courses
    // */comalat/languages/{lang}/levels/{lvl}/courses/{coursesXY}/units/update
    @PUT
    @Path("/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateFile(
            @FormDataParam("uploadFile") InputStream in,
            @FormDataParam("uploadFile") FormDataContentDisposition info,
            @FormDataParam("name") String unit,
            @PathParam("lang") String lang,
            @PathParam("lvl") String lvl,
            @PathParam("coursesXY") String coursesXY) {

        if (in == null || !info.getFileName().endsWith(Constants.PDF_FORMAT)) {
            throw new InvalidInputException("Please select pdf format file");
        }

        if (unit == null || unit.replace(" ", "").isEmpty()) {
            throw new InvalidInputException("Please input file name");
        }

        String source = FolderManager.getPath(FolderManager.getPath(FolderManager.getPath(Constants.SOURCE_FOLDER, lang), lvl), coursesXY);

        if (Paths.get(source, unit) != null) {
            FolderManager.delete(Paths.get(source, unit).toString());
        }
        FolderManager.saveUploadedFile(in, Paths.get(source, unit).toString(), info.getFileName());

        SuccessMessage message = new SuccessMessage("Update " + info.getFileName(), Status.OK.getStatusCode(), null);
        return Response.status(Status.OK).entity(message).build();
    }
}
