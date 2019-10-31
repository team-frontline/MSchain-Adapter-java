package client;

import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FabricClient {

    private HFClient client;

    public HFClient getInstance() {
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
        ChannelClient channelClient = new ChannelClient(name, channel, this);
        return channelClient;
    }

    public Collection<ProposalResponse> deployChaincode(String chaincodeName, String chaincodePath,
                                                        String sourceDirectory, String language, String version,
                                                        Collection<Peer> peers)
            throws InvalidArgumentException, ProposalException {
        InstallProposalRequest request = client.newInstallProposalRequest();
        ChaincodeID.Builder builder = ChaincodeID.newBuilder().setName(chaincodeName).setVersion(version)
                .setPath(chaincodePath);
        ChaincodeID chaincodeID = builder.build();
        Logger.getLogger(FabricClient.class.getName()).log(Level.INFO,
                "Deploying chaincode " + chaincodeName + " using Fabric client " + client.getUserContext().getMspId()
                        + " " + client.getUserContext().getName());
        request.setChaincodeID(chaincodeID);
        request.setUserContext(client.getUserContext());
        request.setChaincodeSourceLocation(new File(sourceDirectory));
        request.setChaincodeVersion(version);
        Collection<ProposalResponse> responses = client.sendInstallProposal(request, peers);
        return responses;
    }


}
