package client;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import user.UserContext;
import util.Util;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CAClient {
    private String caUrl;
    private Properties caProperties;
    private HFCAClient hfcaClient;
    private UserContext adminContext;

    public CAClient(){}

    //should modify
    public CAClient(String caUrl, Properties properties) {
        this.caUrl = caUrl;
        this.caProperties = properties;
        try {
            this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCaUrl() {
        return caUrl;
    }

    public void setCaUrl(String caUrl) {
        this.caUrl = caUrl;
    }

    public Properties getCaProperties() {
        return caProperties;
    }

    public void setCaProperties(Properties caProperties) {
        this.caProperties = caProperties;
    }

    public HFCAClient getHfcaClient() {
        return hfcaClient;
    }

    public void setHfcaClient(HFCAClient hfcaClient) {
        this.hfcaClient = hfcaClient;
    }

    public UserContext getAdminContext() {
        return adminContext;
    }

    public void setAdminContext(UserContext adminContext) {
        this.adminContext = adminContext;
    }

    public void init() throws MalformedURLException, IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException, org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
        hfcaClient = HFCAClient.createNewInstance(caUrl, caProperties);
        hfcaClient.setCryptoSuite(cryptoSuite);
    }

    public UserContext enrollAdminUser(String username, String password) throws Exception{
        UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), username);
        if (userContext != null) {
            Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl + " admin is already enrolled.");
            return userContext;
        }
        Enrollment adminEnrollment = hfcaClient.enroll(username, password);
        adminContext.setEnrollment(adminEnrollment);
        Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl + " Enrolled Admin.");
        Util.writeUserContext(adminContext);
        return adminContext;
    }

    public String registerUser(String username, String organization) throws Exception { return null;}

    public UserContext enrollUser(UserContext user, String secret) throws Exception {return null;}
}
