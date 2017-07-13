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
public class ClientPUT {

    String server = "http://localhost:8080/";

    public ClientPUT() {
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

    // Unit Update
    //@Test
    public void PUTUnitTesting() {
        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");
        WebTarget unitUpdate = courseTarget.path("units/update");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        String unitName = "unit1"; // unit{X} x -> 1-20
        String pdfName = "file1 updated.pdf";
        File pdffile = null;
        try {
            // The location of pdf file
            pdffile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files" ,pdfName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of pdf file!!!");
            return;
        }

        System.out.println("\n..........Starting pdf PUT ..........");

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

        System.out.println("Update : Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "/units/update\n"
                + "Update unit:" + unitName);
        Response unitResponse = unitUpdate
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .request()
                .put(Entity.entity(multiPart, multiPart.getMediaType()));

        if (unitResponse.getStatus() != 200) {
            System.out.println("ERROR AT UPDATE .../units/update\n"
                    + "response: " + unitResponse);
        } else {
            System.out.println("DONE!! \n" + unitResponse);
        }
    }

    //Course update
    //@Test
    public void PUTCourseTesting() {

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseUpdate = eductionLevelTarget.path("courses/update/");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        String zipName = "Courses-1-5.zip";
        File zipFile = null;
        try {
            // The location of zip file
            zipFile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files", zipName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of zip file!!!");
            return;
        }

        System.out.println("\n..........Starting courses PUT ..........");

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
                zipFile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("uploadFile")
                        .fileName(zipFile.getName()).build());

        MultiPart multiPart = new FormDataMultiPart()
                .field("coursesName", courseName, MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        System.out.println("Update : Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/update\n"
                + "Update course:" + courseName);
        Response courseResponse = courseUpdate
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .request()
                .put(Entity.entity(multiPart, multiPart.getMediaType()));

        if (courseResponse.getStatus() != 200) {
            System.out.println("ERROR AT UPDATE .../courses/update\n"
                    + "response: " + courseResponse);
        } else {
            System.out.println("DONE!! \n" + courseResponse);
        }
    }
    
    //Education Level update
    //@Test
    public void PUTLevelTesting() {

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget levelUpdate = languageTarget.path("levels/update");

        String language = "spanish"; // language
        String lvlName = "elementary"; // elementary or intermediate
        String zipName = "Elementary.zip";
        File zipFile = null;
        try {
            // The location of zip file
            zipFile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files", zipName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of zip file!!!");
            return;
        }

        System.out.println("\n..........Starting education level PUT ..........");

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
                zipFile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("uploadFile")
                        .fileName(zipFile.getName()).build());

        MultiPart multiPart = new FormDataMultiPart()
                .field("lvlName", lvlName, MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        System.out.println("Update : Resource: /comalat/languages/" + language+ "/levels/update\n"
                + "Update level:" + lvlName);
        Response levelResponse = levelUpdate
                .resolveTemplate("lang", language)
                .request()
                .put(Entity.entity(multiPart, multiPart.getMediaType()));

        if (levelResponse.getStatus() != 200) {
            System.out.println("ERROR AT UPDATE .../levels/update\n"
                    + "response: " + levelResponse);
        } else {
            System.out.println("DONE!! \n" + levelResponse);
        }
    }
    
    //Language update
    //@Test
    public void PUTLanguageTesting() {

        Client client = ClientBuilder.newBuilder()
                .register(MultiPartFeature.class).build();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget langUpdate = languagesTarget.path("update");

        String langName = "spanish"; // language
        String zipName = "Spanish.zip";
        File zipFile = null;
        try {
            // The location of zip file
            zipFile = new File(Paths.get(Constants.USER_DIR, "Desktop", "PUT POST files", zipName).toString());
        } catch (NullPointerException ex) {
            System.err.println("Check the location of zip file!!!");
            return;
        }

        System.out.println("\n..........Starting language PUT ..........");

        FileDataBodyPart fileDataBodyPart = new FileDataBodyPart("uploadFile",
                zipFile,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);

        fileDataBodyPart.setContentDisposition(
                FormDataContentDisposition.name("uploadFile")
                        .fileName(zipFile.getName()).build());

        MultiPart multiPart = new FormDataMultiPart()
                .field("langName", langName, MediaType.TEXT_PLAIN_TYPE)
                .bodyPart(fileDataBodyPart);
        multiPart.setMediaType(MediaType.MULTIPART_FORM_DATA_TYPE);

        System.out.println("Update : Resource: /comalat/languages/update\n"
                + "Update language:" + langName);
        Response langResponse = langUpdate
                .request()
                .put(Entity.entity(multiPart, multiPart.getMediaType()));

        if (langResponse.getStatus() != 200) {
            System.out.println("ERROR AT UPDATE .../languages/update\n"
                    + "response: " + langResponse);
        } else {
            System.out.println("DONE!! \n" + langResponse);
        }
    }
}
