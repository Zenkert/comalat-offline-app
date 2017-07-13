/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAPI;

import comalat.Constants;
import java.io.File;
import java.nio.file.Paths;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author SyleSakis
 */
public class ClientPOST {

    String server = "http://localhost:8080/";

    public ClientPOST() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    // Language Post
    @Test
    public void POSTLanguageTesting() {

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server+"offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");

        String langName = "spanish"; // language
        String zipName = "Spanish.zip";
        File zipfile = null;
        try {
            // The location of zip file
            zipfile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files", zipName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of zip file!!!");
            return;
        }

        System.out.println("\n..........Starting POST Call..........");

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
                zipfile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("uploadFile")
                        .fileName(zipfile.getName()).build());

        MultiPart multiPart = new FormDataMultiPart()
                .field("langName", langName, MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        System.out.println("Upload: Resource: /comalat/languages/upload\n"
                + "Upload a new language");
        Response languageResponse = languagesTarget.path("upload")
                .request()
                .post(Entity.entity(multiPart, multiPart.getMediaType()));

        if (languageResponse.getStatus() == 201) {
            System.out.println("DONE!! \n" + languageResponse);
        }else if (languageResponse.getStatus() == 409) {
            System.out.println("Conflict use PUT for update \n"
                    + "response: " + languageResponse);
        } else {
            System.out.println("ERROR AT UPLOAD /comalat/languages/upload\n"
                    + "response: " + languageResponse);
        }

    }

    // Unit Post
    //@Test
    public void POSTUnitTesting() {

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server+"offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");
        WebTarget unitUpload = courseTarget.path("units/upload");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        String unitName = "unit3"; // unit{X} x -> 1-20
        String pdfName = "file1 updated.pdf";
        File pdffile = null;
        try {
            // The location of zip file
            pdffile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files", pdfName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of pdf file!!!");
            return;
        }

        System.out.println("\n..........Starting pdf POST ..........");

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
                pdffile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("uploadFile")
                        .fileName(pdffile.getName()).build());

        MultiPart multiPart = new FormDataMultiPart()
                .field("unitName", unitName, MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

       System.out.println("Upload : Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "/units/upload\n"
                + "Upload unit:" + unitName);
        Response unitResponse = unitUpload
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .request()
                .post(Entity.entity(multiPart, multiPart.getMediaType()));

        if (unitResponse.getStatus() != 201) {
            System.out.println("ERROR AT UPLOAD .../units/upload\n"
                    + "response: " + unitResponse);
        } else {
            System.out.println("DONE!! \n" + unitResponse);
        }
    }
}
