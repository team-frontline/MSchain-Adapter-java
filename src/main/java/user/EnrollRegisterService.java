package user;

import client.CAClient;
import config.Config;
import util.Util;

public class EnrollRegisterService {
    public static void main(String args[]) {
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG1_URL;
            //String caTLSCACertPath = "/home/supimi/hyperledger/fabric-samples/first-network/crypto-config/peerOrganizations/org2.example.com/tlsca/tlsca.org2.example.com-cert.pem";

            //Properties caProperties = new Properties();
            //byte[] keyBytes = Files.readAllBytes(Paths.get(caTLSCACertPath));
            //caProperties.put("pemBytes", keyBytes);

            CAClient caClient = new CAClient(caUrl, null);
            //System.out.println(caClient.getHfcaClient().getStatusCode());
            //caClient.getInstance().info();


            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(Config.ADMIN);
            adminUserContext.setAffiliation(Config.ORG1);
            adminUserContext.setMspId(Config.ORG1_MSP);
            caClient.setAdminContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            // Register and Enroll user to Org1MSP
            UserContext userContext = new UserContext();
            String name = "user"+System.currentTimeMillis();
            userContext.setName(name);
            userContext.setAffiliation(Config.ORG1);
            userContext.setMspId(Config.ORG1_MSP);
            String eSecret = caClient.registerUser(name, Config.ORG1);

            userContext = caClient.enrollUser(userContext, eSecret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
