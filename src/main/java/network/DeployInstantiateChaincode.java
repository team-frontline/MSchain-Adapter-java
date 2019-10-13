package network;

import client.FabricClient;
import config.Config;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import user.UserContext;
import util.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DeployInstantiateChaincode {

    public static void main(String[] args) {
        try {
            CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();

            UserContext admin_org1 = new UserContext();
            File skFolder1 = new File(Config.ORG1_ADMIN_PK_PATH);
            File[] skFiles1 = skFolder1.listFiles();
            File certFolder1 = new File(Config.ORG1_ADMIN_CERT_PATH);
            File[] certFiles1 = certFolder1.listFiles();
            Enrollment enrollmentOrg1Admin = Util.getEnrollment(Config.ORG1_ADMIN_PK_PATH, skFiles1[0].getName(),
                    Config.ORG1_ADMIN_CERT_PATH, certFiles1[0].getName());
            admin_org1.setEnrollment(enrollmentOrg1Admin);
            admin_org1.setMspId(Config.ORG1_MSP);
            admin_org1.setName(Config.ADMIN);

            UserContext admin_org2 = new UserContext();
            File skFolder2 = new File(Config.ORG2_ADMIN_PK_PATH);
            File[] skFiles2 = skFolder2.listFiles();
            File certFolder2 = new File(Config.ORG2_ADMIN_CERT_PATH);
            File[] certFiles2 = certFolder2.listFiles();
            Enrollment enrollmentOrg2Admin = Util.getEnrollment(Config.ORG2_ADMIN_PK_PATH, skFiles2[0].getName(),
                    Config.ORG2_ADMIN_CERT_PATH, certFiles2[0].getName());
            admin_org2.setEnrollment(enrollmentOrg2Admin);
            admin_org2.setMspId(Config.ORG2_MSP);
            admin_org2.setName(Config.ADMIN);

            //new client as org1 adimin as the user
            FabricClient fabricClient = new FabricClient(admin_org1);

            Channel msChaneel = fabricClient.getInstance().newChannel(Config.CHANNEL_NAME);
            Orderer orderer = fabricClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            Peer peer0_org1 = fabricClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            Peer peer1_org1 = fabricClient.getInstance().newPeer(Config.ORG1_PEER_1, Config.ORG1_PEER_1_URL);
            Peer peer0_org2 = fabricClient.getInstance().newPeer(Config.ORG2_PEER_0, Config.ORG2_PEER_0_URL);
            Peer peer1_org2 = fabricClient.getInstance().newPeer(Config.ORG2_PEER_1, Config.ORG2_PEER_1_URL);

            msChaneel.addOrderer(orderer);
            msChaneel.addPeer(peer0_org1);
            msChaneel.addPeer(peer1_org1);
            msChaneel.addPeer(peer0_org2);
            msChaneel.addPeer(peer1_org2);
            msChaneel.initialize();

            List<Peer> peersOrg1 = new ArrayList<>();
            peersOrg1.add(peer0_org1);
            peersOrg1.add(peer1_org1);

            List<Peer> peersOrg2 = new ArrayList<>();
            peersOrg1.add(peer0_org2);
            peersOrg1.add(peer1_org2);

            Collection<ProposalResponse> responses = fabricClient.deployChaincode(Config.CHAINCODE_NAME,
                    Config.CHAINCODE_PATH, Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(), Config.CHAINCODE_VERSION,
                    peersOrg1);

            for (ProposalResponse response : responses) {
                Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
                        Config.CHAINCODE_NAME + "- Chain code deployment " + response.getStatus());
            }

            //set org2 admin as the user
            fabricClient.getInstance().setUserContext(admin_org2);
            responses = fabricClient.deployChaincode(Config.CHAINCODE_NAME,
                    Config.CHAINCODE_PATH, Config.CHAINCODE_ROOT_DIR, Type.GO_LANG.toString(), Config.CHAINCODE_VERSION,
                    peersOrg2);

            for (ProposalResponse response : responses) {
                Logger.getLogger(DeployInstantiateChaincode.class.getName()).log(Level.INFO,
                        Config.CHAINCODE_NAME + "- Chain code deployment " + response.getStatus());
            }

            //instantiation should be done





        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
