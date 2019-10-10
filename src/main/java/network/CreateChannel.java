package network;

import org.hyperledger.fabric.sdk.security.CryptoSuite;
import user.UserContext;
import util.Util;

public class CreateChannel {

    public static void main(String[] args) {
        try {
            CryptoSuite.Factory.getCryptoSuite();
            Util.cleanUp();
            UserContext admin1 = new UserContext();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
