package comalat.Services.FileServices;

import comalat.Application.Exception.DataNotFoundException;
import comalat.Application.Exception.ServerProcedureException;
import comalat.Constants;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 *
 * @author SyleSakis
 */
public class AccessData {

    private static final String USERNAME = "USERNAME:";
    private static final String PASSWORD = "PASSWORD:";
    private static final String FULLNAME = "FULLNAME:";

    private static final Path filepath = Paths.get(Constants.ACCESS_DATA);
    private static final String SHA256 = "SHA-256";

    private static final String NEWLINE = System.getProperty("line.separator");

    /*
     * Create access data file.
     * @throws ServerProcedureException if something goes wrong.
     */
    public static void createAccessFile() {
        if (!filepath.toFile().exists()) {
            writeAccessFile("admin", "admin", "Admin");
        }
    }

    /*
     * Update access data file.
     * @throws ServerProcedureException if something goes wrong.
     */
    public static void updateAccessFile(String username, String password, String fullname) {
        writeAccessFile(username, password, fullname);
    }

    /*
     * Compare Data access data file.
    */
    public static boolean compareData(String username, String password) {
        return getUsername().equals(encrypt(username)) && getPassword().equals(encrypt(password));
    }

    public static final String getUsername() {
        return readAccessFile(USERNAME);
    }

    public static final String getPassword() {
        return readAccessFile(PASSWORD);
    }

    public static final String getFullname() {
        return readAccessFile(FULLNAME);
    }

    /*
     * Encrypt access file.
     * @throws ServerProcedureException if something goes wrong.
    */
    private static String encrypt(String key) {
        MessageDigest md;

        try {
            md = MessageDigest.getInstance(SHA256);
            md.update(key.getBytes());
            byte[] aMessageDigest = md.digest();
            String outEncoded = Base64.getEncoder().encodeToString(aMessageDigest);
            return (outEncoded);
        } catch (NoSuchAlgorithmException ex) {
            throw new ServerProcedureException("Problem with encrypt");
        }
    }

    private static String readAccessFile(String key) {
        try {
            List<String> lines = Files.readAllLines(filepath, Charset.defaultCharset());
            for (String line : lines) {
                if (line.contains(key)) {
                    return line.replace(key, "");
                }
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            throw new ServerProcedureException("Problem with reading Access file");
        }
        throw new DataNotFoundException("Problem with Access file can't find key {" + key + "}");
    }

    private static void writeAccessFile(String username, String password, String fullname) {

        String accessdata = "*** Comalat Access ***" + NEWLINE
                + "USERNAME:" + encrypt(username) + NEWLINE
                + "PASSWORD:" + encrypt(password) + NEWLINE
                + "FULLNAME:" + fullname;

        try {
            Files.write(filepath, accessdata.getBytes());
        } catch (IOException ex) {
            throw new ServerProcedureException("Problem with writing Access file");
        }
    }
}
