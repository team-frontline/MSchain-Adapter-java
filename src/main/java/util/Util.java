package util;

import user.CAEnrollment;
import user.UserContext;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Util {

    public static void cleanUp() {
        String directoryPath = "users";
        File directory = new File(directoryPath);
        deleteDirectory(directory);
    }

    public static boolean deleteDirectory(File dir) {
        if (dir.isDirectory()) {
            File[] children = dir.listFiles();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDirectory(children[i]);
                if (!success) {
                    return false;
                }
            }
        }

        // either file or an empty directory
        Logger.getLogger(Util.class.getName()).log(Level.INFO, "Deleting - " + dir.getName());
        return dir.delete();
    }

    public static CAEnrollment getEnrollment(String skFolderPath, String skFileName, String certFolderPath, String certFileName)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        PrivateKey key;
        String certificate;
        InputStream streamKey = null;
        BufferedReader bufferedReaderKey = null;

        try {
            streamKey = new FileInputStream(skFolderPath + File.separator + skFileName);
            bufferedReaderKey = new BufferedReader(new InputStreamReader(streamKey));
            StringBuilder keyStringBuilder = new StringBuilder();
            for (String line = bufferedReaderKey.readLine(); line != null; line = bufferedReaderKey.readLine()) {
                if (!line.contains("PRIVATE")) {
                    keyStringBuilder.append(line);
                }
            }
            certificate = new String(Files.readAllBytes(Paths.get(certFolderPath, certFileName)));

            byte[] encoded = DatatypeConverter.parseBase64Binary(keyStringBuilder.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            KeyFactory kf = KeyFactory.getInstance("EC");
            key = kf.generatePrivate(keySpec);

        } finally {
            streamKey.close();
            bufferedReaderKey.close();
        }
        CAEnrollment caEnrollment = new CAEnrollment(key, certificate);
        return caEnrollment;
    }

    public static UserContext readUserContext(String affiliation, String username) throws Exception {
        String filePath = "users/" + affiliation + "/" + username + ".ser";
        File file = new File(filePath);
        if (file.exists()) {
            // Reading the object from a file
            FileInputStream fileStream = new FileInputStream(filePath);
            ObjectInputStream inputStream = new ObjectInputStream(fileStream);

            // Method for deserialization of object
            UserContext uContext = (UserContext) inputStream.readObject();

            inputStream.close();
            fileStream.close();
            return uContext;
        }
        return null;
    }

    public static void writeUserContext(UserContext userContext) throws IOException {
        String directoryPath = "users/" + userContext.getAffiliation();
        String filePath = directoryPath + "/" + userContext.getName() + ".ser";
        File directory = new File(directoryPath);
        if (!directory.exists())
            directory.mkdirs();

        FileOutputStream file = new FileOutputStream(filePath);
        ObjectOutputStream outputStream = new ObjectOutputStream(file);

        // Method for serialization of object
        outputStream.writeObject(userContext);

        outputStream.close();
        file.close();
    }
}
