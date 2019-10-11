package client;

import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.lang.reflect.InvocationTargetException;

public class FabricClient {

    private HFClient client;

    public HFClient getInstance(){
        return client;
    }

    public FabricClient(User context) throws CryptoException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

        client = HFClient.createNewInstance();
        client.setCryptoSuite(cryptoSuite);
        client.setUserContext(context);
    }

    public ChannelClient createChannelClient(String name) throws InvalidArgumentException {
        Channel channel = client.newChannel(name);
//        ChannelClient client = new ChannelClient(name, channel, this);
        return null;
    }


}
