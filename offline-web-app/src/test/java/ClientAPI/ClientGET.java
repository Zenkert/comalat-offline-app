/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAPI;

import comalat.Constants;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
public class ClientGET {

    String server = "http://localhost:8080/";

    public ClientGET() {
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

    // save file to local machine
    // USER_DIR/Desktop/filename
    private void writeFile(InputStream is, String name) {
        
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(
                    Paths.get(Constants.USER_DIR, "Desktop", name).toString());
            int len = 0;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }   out.flush();
            out.close();
            is.close();
        } catch (IOException ex) {
            return;
        }
    }

    @Test
    public void GETTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget dataTarget = baseTarget.path("data/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");

        System.out.println("\n..........Starting GET Calls..........");

        System.out.println("GET 1: Resource: /comalat/data");
        Response dataResponse = dataTarget
                .request()
                .get();
        if (dataResponse.getStatus() != 200) {
            System.out.println("ERROR AT GET /comalat/data\n" + dataResponse);
        } else {
            System.out.println(dataResponse);
        }

        System.out.println("\nGET 2: Resource: " + "/comalat/languages/english/levels/education-not-found");
        Response errorResponse = eductionLevelTarget
                .resolveTemplate("lang", "english")
                .resolveTemplate("lvl", "education-not-found")
                .request()
                .get();
        if (errorResponse.getStatus() != 200) {
            System.out.println("ERROR AT GET /comalat/languages/english/levels/education-not-found\n" + errorResponse);
        } else {
            System.out.println(errorResponse);
        }
    }

    // Unit Get
    //@Test
    public void GETUnitTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");
        WebTarget unitTarget = courseTarget.path("units/{unit}");

        System.out.println("\n..........Starting GET Unit..........");
        String language = "english"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        String unitName = "unit1"; // unit{X} x -> 1-20
        // header
        long serialNo = Date.from(Instant.now()).getTime();

        System.out.println("GET: Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "/units/" + unitName + "\n"
                + "GET unit:" + unitName);
        Response unitResponse = unitTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .resolveTemplate("unit", unitName)
                .request()
                //.header("serialNo", serialNo)
                .get();
        if (unitResponse.getStatus() == 200) {
            System.out.println(unitResponse);
            InputStream is = (InputStream) unitResponse.getEntity();
            writeFile(is, unitName+".pdf");

        } else if (unitResponse.getStatus() == 404) {
            System.out.println("NOT FOUND!! \n" + unitResponse);
        } else {
            System.out.println("ERROR AT GET .../units/{unit}\n" + unitResponse);

        }
    }
    
    // Course Get
    //@Test
    public void GETCourseTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");

        System.out.println("\n..........Starting GET course..........");
        String language = "english"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        // header
        Random rand = new Random();
        long serialNo = (long)rand.nextInt((1000 - 10) + 1) + 10;

        System.out.println("GET: Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "\n"
                + "GET course:" + courseName);
        Response courseResponse = courseTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .request()
                .header("serialNo", serialNo)
                .get();
        if (courseResponse.getStatus() == 200) {
            System.out.println(courseResponse);
            InputStream is = (InputStream) courseResponse.getEntity();
            writeFile(is, courseName+".zip");

        } else if (courseResponse.getStatus() == 404) {
            System.out.println("NOT FOUND!! \n" + courseResponse);
        } else {
            System.out.println("ERROR AT GET .../courses/{coursesXY}\n" + courseResponse);

        }
    }
    
    // Education level Get
    //@Test
    public void GETLevelTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");

        System.out.println("\n..........Starting GET level..........");
        String language = "english"; // language
        String lvl = "intermediate"; // elementary or intermediate
        // header
        Random rand = new Random();
        long serialNo = (long)rand.nextInt((1000 - 10) + 1) + 10;

        System.out.println("GET: Resource: /comalat/languages/" + language + "/levels/" + lvl + "\n"
                + "GET level:" + lvl);
        Response levelResponse = eductionLevelTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .request()
                .header("serialNo", serialNo)
                .get();
        if (levelResponse.getStatus() == 200) {
            System.out.println(levelResponse);
            InputStream is = (InputStream) levelResponse.getEntity();
            writeFile(is, lvl+".zip");

        } else if (levelResponse.getStatus() == 404) {
            System.out.println("NOT FOUND!! \n" + levelResponse);
        } else {
            System.out.println("ERROR AT GET .../levels/{lvl}\n" + levelResponse);

        }
    }
    
     // Language Get
    //@Test
    public void GETLanguageTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");

        System.out.println("\n..........Starting GET level..........");
        String language = "english"; // language
        // header
        Random rand = new Random();
        long serialNo = (long)rand.nextInt((1000 - 10) + 1) + 10;

        System.out.println("GET: Resource: /comalat/languages/" + language + "\n"
                + "GET language:" + language);
        Response languageResponse = languageTarget
                .resolveTemplate("lang", language)
                .request()
                .header("serialNo", serialNo)
                .get();
        if (languageResponse.getStatus() == 200) {
            System.out.println(languageResponse);
            InputStream is = (InputStream) languageResponse.getEntity();
            writeFile(is, language+".zip");

        } else if (languageResponse.getStatus() == 404) {
            System.out.println("NOT FOUND!! \n" + languageResponse);
        } else {
            System.out.println("ERROR AT GET .../languages/{lang}\n" + languageResponse);

        }
    }
    
    // Languages Get
    @Test
    public void GETLanguagesTesting() {

        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");

        System.out.println("\n..........Starting GET level..........");
        // header
        Random rand = new Random();
        long serialNo = (long)rand.nextInt((1000 - 10) + 1) + 10;

        System.out.println("GET: Resource: /comalat/languages \n"
                + "GET languages");
        Response languagesResponse = languagesTarget
                .request()
                .header("serialNo", serialNo)
                .get();
        if (languagesResponse.getStatus() == 200) {
            System.out.println(languagesResponse);
            InputStream is = (InputStream) languagesResponse.getEntity();
            writeFile(is, "languages.zip");

        } else if (languagesResponse.getStatus() == 404) {
            System.out.println("NOT FOUND!! \n" + languagesResponse);
        } else {
            System.out.println("ERROR AT GET .../languages\n" + languagesResponse);

        }
    }

}
