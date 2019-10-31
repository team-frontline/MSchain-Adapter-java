package chaincode;

import client.CAClient;
import client.ChannelClient;
import client.FabricClient;
import config.Config;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import user.UserContext;
import util.Util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InvokeQueryChaincode {
    public static void main(String[] args) {
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);
            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(Config.ADMIN);
            adminUserContext.setAffiliation(Config.ORG1);
            adminUserContext.setMspId(Config.ORG1_MSP);
            caClient.setAdminContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            FabricClient fabClient = new FabricClient(adminUserContext);

            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();
            Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            channel.addPeer(peer);
            channel.addEventHub(eventHub);
            channel.addOrderer(orderer);
            channel.initialize();

            String[] arguments = {"example.edu"};

            ArrayList<ProposalResponse> responsesQuery = (ArrayList<ProposalResponse>) channelClient.queryByChainCode("ctb", "queryCertificate", arguments);
            if (responsesQuery.get(0).getChaincodeActionResponseStatus() != 200) {
                Logger.getLogger(InvokeQueryChaincode.class.getName()).log(Level.WARNING, responsesQuery.get(0).getMessage());
            }
            for (ProposalResponse pres : responsesQuery) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                System.out.println(stringResponse);
                Logger.getLogger(InvokeQueryChaincode.class.getName()).log(Level.INFO, stringResponse);
            }

        } catch (InvalidArgumentException invalidArgumentException) {
            Logger.getLogger(InvokeQueryChaincode.class.getName()).log(Level.WARNING, invalidArgumentException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
