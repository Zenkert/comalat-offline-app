/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientAPI;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
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
public class ClientDELETE {

    String server = "http://localhost:8080/";

    public ClientDELETE() {
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

    // Unit Delete
    //@Test
    public void DELETEUnitTesting() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");
        WebTarget unitTarget = courseTarget.path("units/{unit}");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20
        String unitName = "unit1"; // unit{X} x -> 1-20

        System.out.println("\n..........Starting pdf DELETE ..........");

        System.out.println("Delete : Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "/units/"+unitName+"\n"
                + "Delete unit:" + unitName);
        Response unitResponse = unitTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .resolveTemplate("unit", unitName)
                .request()
                .delete();

        if (unitResponse.getStatus() == 200) {
            System.out.println("DONE!! \n" + unitResponse);
        }else if(unitResponse.getStatus() == 404){
            System.out.println("NOT FOUND!! \n" + unitResponse);
        }else {
            System.out.println("ERROR AT DELETE .../units/unit\n response: " + unitResponse);
        }
    }
    
    // Course Delete
    //@Test
    public void DELETECourseTesting() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");
        WebTarget courseTarget = eductionLevelTarget.path("courses/{coursesXY}/");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate
        String courseName = "courses-1-5"; // courses-{X}-{Y} x -> 1,6,11,16 & y -> 5,10,15,20

        System.out.println("\n..........Starting course DELETE ..........");

        System.out.println("Delete : Resource: /comalat/languages/" + language
                + "/levels/" + lvl + "/courses/" + courseName + "\n"
                + "Delete course:" + courseName);
        Response courseResponse = courseTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .resolveTemplate("coursesXY", courseName)
                .request()
                .delete();

        if (courseResponse.getStatus() == 200) {
            System.out.println("DONE!! \n" + courseResponse);
        }else if(courseResponse.getStatus() == 404){
            System.out.println("NOT FOUND!! \n" + courseResponse);
        }else {
            System.out.println("ERROR AT DELETE .../courses/{courses}\n response: " + courseResponse);
        }
    }
    
    // Education Level Delete
    //@Test
    public void DELETELevelTesting() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");
        WebTarget eductionLevelTarget = languageTarget.path("levels/{lvl}/");

        String language = "spanish"; // language
        String lvl = "elementary"; // elementary or intermediate

        System.out.println("\n..........Starting level DELETE ..........");

        System.out.println("Delete : Resource: /comalat/languages/" + language + "/levels/" + lvl + "\n"
                + "Delete level:" + lvl);
        Response levelResponse = eductionLevelTarget
                .resolveTemplate("lang", language)
                .resolveTemplate("lvl", lvl)
                .request()
                .delete();

        if (levelResponse.getStatus() == 200) {
            System.out.println("DONE!! \n" + levelResponse);
        }else if(levelResponse.getStatus() == 404){
            System.out.println("NOT FOUND!! \n" + levelResponse);
        }else {
            System.out.println("ERROR AT DELETE .../levels/{lvl}\n response: " + levelResponse);
        }
    }
    
    // Language Delete
    //@Test
    public void DELETELanguageTesting() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");
        WebTarget languageTarget = languagesTarget.path("{lang}/");

        String language = "spanish"; // language

        System.out.println("\n..........Starting language DELETE ..........");

        System.out.println("Delete : Resource: /comalat/languages/" + language
                + "Delete language:" + language);
        Response languageResponse = languageTarget
                .resolveTemplate("lang", language)
                .request()
                .delete();

        if (languageResponse.getStatus() == 200) {
            System.out.println("DONE!! \n" + languageResponse);
        }else if(languageResponse.getStatus() == 404){
            System.out.println("NOT FOUND!! \n" + languageResponse);
        }else {
            System.out.println("ERROR AT DELETE .../languages/{lang}\n response: " + languageResponse);
        }
    }
    
    // ALL Languages Delete
    //@Test
    public void DELETELanguagesTesting() {
        Client client = ClientBuilder.newClient();
        WebTarget baseTarget = client.target(server + "offline-web-app/comalat/");
        WebTarget languagesTarget = baseTarget.path("languages/");

        System.out.println("\n..........Starting DELETE all languages ..........");

        System.out.println("Delete : Resource: /comalat/languages/" +"\n"
                + "Delete languages");
        Response languagesResponse = languagesTarget
                .request()
                .delete();

        if (languagesResponse.getStatus() == 200) {
            System.out.println("DONE!! \n" + languagesResponse);
        }else if(languagesResponse.getStatus() == 404){
            System.out.println("NOT FOUND!! \n" + languagesResponse);
        }else {
            System.out.println("ERROR AT DELETE .../languages\n response: " + languagesResponse);
        }
    }
}
