package comalat.Application.Resources;

import comalat.Application.Domain.ErrorMessage;
import comalat.Application.Exception.DataNotFoundException;
import comalat.Constants;
import comalat.Services.FolderServices.CompressManager;
import comalat.Services.FolderServices.FolderManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.util.List;
import java.util.Map;
import javax.ws.rs.FormParam;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 *
 * @author SyleSakis
 */
@Path("languages")
public class LanguagesResources {

    // return zip file with all languages
    @GET
    public Response getAllLangauge(@HeaderParam("serialNo") long serialNo) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }
        String foldername = "langs_" + serialNo + Constants.ZIP_FORMAT;

        CompressManager.Compression(Constants.SOURCE_FOLDER, Constants.DESTINATION_DOWNLOAD_FOLDER, foldername);
        File file = new File(Paths.get(Constants.DESTINATION_DOWNLOAD_FOLDER, foldername).toString());
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"Languages.zip\"")
                .build();
    }

    // delete source folder    
    @DELETE
    public Response deleteAllLangauges() {
        if (FolderManager.delete(Constants.SOURCE_FOLDER)) {
            return Response.ok()
                    .build();
        }
        return Response.status(Response.Status.NOT_MODIFIED)
                .build();
    }

    @GET
    @Path("/{lang}")
    public Response getLanguage(@HeaderParam("serialNo") long serialNo,
            @PathParam("lang") String lang) {

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }
        String foldername = lang + "_" + serialNo + Constants.ZIP_FORMAT;
        String source = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);

        CompressManager.Compression(source, Constants.DESTINATION_DOWNLOAD_FOLDER, foldername);
        File file = new File(Paths.get(Constants.DESTINATION_DOWNLOAD_FOLDER, foldername).toString());
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + lang + Constants.ZIP_FORMAT + "\"")
                .build();
    }

    @DELETE
    @Path("/{lang}")
    public Response deleteLanguage(@PathParam("lang") String lang) {
        String source = FolderManager.getPath(Constants.SOURCE_FOLDER, lang);
        if (FolderManager.delete(source)) {
            return Response.ok()
                    .build();
        }
        return Response.status(Response.Status.NOT_MODIFIED)
                .build();
    }

    @POST
    //@Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadZippedFile(MultipartFormDataInput multipartFormDataInput) {
        // local variables
        MultivaluedMap<String, String> multivaluedMap = null;
        String fileName = null;
        InputStream inputStream = null;
        String uploadFilePath = null;

        try {
            Map<String, List<InputPart>> map = multipartFormDataInput.getFormDataMap();
            List<InputPart> lstInputPart = map.get("uploadFile");

            for (InputPart inputPart : lstInputPart) {

                // get filename to be uploaded
                multivaluedMap = inputPart.getHeaders();
                fileName = multipartFormDataInput.getFormDataPart("filename", String.class, null) + ".zip";
                //fileName = getFileName(multivaluedMap);

                if (null != fileName && !"".equalsIgnoreCase(fileName)) {

                    // write & upload file to UPLOAD_FILE_SERVER
                    inputStream = inputPart.getBody(InputStream.class, null);
                    uploadFilePath = writeToFileServer(inputStream, fileName);

                    // close the stream
                    inputStream.close();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            // release resources, if any
        }
        return Response.ok("File uploaded successfully at " + uploadFilePath).build();
    }

    private String writeToFileServer(InputStream inputStream, String fileName) throws IOException {

        OutputStream outputStream = null;
        String qualifiedUploadFilePath = Paths.get(Constants.DESTINATION_UPLOAD_FOLDER, fileName).toString();

        try {
            outputStream = new FileOutputStream(new File(qualifiedUploadFilePath));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            outputStream.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            //release resource, if any
            outputStream.close();
        }
        return qualifiedUploadFilePath;
    }

    private String getFileName(MultivaluedMap<String, String> multivaluedMap) {

        String[] contentDisposition = multivaluedMap.getFirst("Content-Disposition").split(";");

        for (String filename : contentDisposition) {

            if ((filename.trim().startsWith("filename"))) {
                String[] name = filename.split("=");
                String exactFileName = name[1].trim().replaceAll("\"", "");
                return exactFileName;
            }
        }
        return "UnknownFile";
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadZip(MultipartFormDataInput multipartFormDataInput,
            @HeaderParam("serialNo") long serialNo) {
        // local variables
        MultivaluedMap<String, String> multivaluedMap = null;
        String fileName = null;
        InputStream inputStream = null;

        if (serialNo == 0) {
            serialNo = Date.from(Instant.now()).getTime();
        }

        try {
            String langName = multipartFormDataInput.getFormDataPart("langName", String.class, null);
            if (langName != null) {
                if (FolderManager.exist(Constants.SOURCE_FOLDER, langName)) {
                    System.out.println("*******************ERROR!! FILE EXIST!");
                    //throw InfoException language exist.                    
                }
            } else {
                // throw exception UserInfo insert langName
            }
            fileName = langName + serialNo + Constants.ZIP_FORMAT;
        } catch (IOException ex) {
            Logger.getLogger(LanguagesResources.class.getName()).log(Level.SEVERE, null, ex);
            // exception Server General exception
        }

        try {
            Map<String, List<InputPart>> map = multipartFormDataInput.getFormDataMap();
            List<InputPart> lstInputPart = map.get("uploadFile");

            for (InputPart inputPart : lstInputPart) {
                
                multivaluedMap = inputPart.getHeaders();
                inputStream = inputPart.getBody(InputStream.class, null);
                FolderManager.saveUploadedFile(Constants.DESTINATION_UPLOAD_FOLDER, inputStream, fileName);
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            // exception
        } finally {
            if(FolderManager.exist(Constants.DESTINATION_UPLOAD_FOLDER, fileName)){
                CompressManager.Decompression(Constants.DESTINATION_UPLOAD_FOLDER, Constants.SOURCE_FOLDER, fileName);
            }
        }
        return Response.ok("File uploaded successfully " + fileName).build();
    }
}
