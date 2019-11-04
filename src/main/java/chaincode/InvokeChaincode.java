package chaincode;

import client.CAClient;
import client.ChannelClient;
import client.FabricClient;
import config.Config;
import org.hyperledger.fabric.sdk.*;
import user.UserContext;
import util.Util;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class InvokeChaincode {

    private static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    private static final String EXPECTED_EVENT_NAME = "event";

    public static void main(String args[]) {
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

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_NAME).build();
            request.setChaincodeID(ccid);


            /*
            Parameters for addCertificate
            certString := args[0]
            intermediateCertString := args[1]
            sigString := args[2]
            */
            request.setFcn("addCertificate");

            String[] chainCodeArgs = new String[3];
            chainCodeArgs[0] = Util.readFileToString(Paths.get(Config.TEST_MS_CERT_PATH));
            chainCodeArgs[1] = Util.readFileToString(Paths.get(Config.TEST_CA_CERT_PATH));
            chainCodeArgs[2] = Util.readFileToString(Paths.get(Config.TEST_MS_CERT_SIG_PATH));
            request.setArgs(chainCodeArgs);
            request.setProposalWaitTime(1000);

            //Transient Map is required
            Map<String, byte[]> transientMap = new HashMap<>();
            transientMap.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            transientMap.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            transientMap.put("result", ":)".getBytes(UTF_8));
            transientMap.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(transientMap);

            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(InvokeChaincode.class.getName()).log(Level.INFO, "Invoked createCar on " +
                        Config.CHAINCODE_NAME + ". Status - " + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
