package network;

import client.FabricClient;
import config.Config;
import org.hyperledger.fabric.sdk.*;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import user.UserContext;
import util.Util;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateChannel {

    public static void main(String[] args) {
        try {
            CryptoSuite.Factory.getCryptoSuite();
            Util.cleanUp();

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

            FabricClient fabricClient = new FabricClient(admin_org1);

            Orderer orderer = fabricClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            ChannelConfiguration channelConfiguration = new ChannelConfiguration(new File(Config.CHANNEL_CONFIG_PATH));

            byte[] channelConfigurationSignatures = fabricClient.getInstance()
                    .getChannelConfigurationSignature(channelConfiguration, admin_org1);

            Channel msChannel = fabricClient.getInstance().newChannel(Config.CHANNEL_NAME, orderer, channelConfiguration,
                    channelConfigurationSignatures);

            Peer peer0_org1 = fabricClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            Peer peer1_org1 = fabricClient.getInstance().newPeer(Config.ORG1_PEER_1, Config.ORG1_PEER_1_URL);
            Peer peer0_org2 = fabricClient.getInstance().newPeer(Config.ORG2_PEER_0, Config.ORG2_PEER_0_URL);
            Peer peer1_org2 = fabricClient.getInstance().newPeer(Config.ORG2_PEER_1, Config.ORG2_PEER_1_URL);

            msChannel.joinPeer(peer0_org1);
            msChannel.joinPeer(peer1_org1);
            msChannel.addOrderer(orderer);
            msChannel.initialize();

            fabricClient.getInstance().setUserContext(admin_org2);
            msChannel = fabricClient.getInstance().getChannel("MSchannel");
            msChannel.joinPeer(peer0_org2);
            msChannel.joinPeer(peer1_org2);

            Logger.getLogger(CreateChannel.class.getName()).log(Level.INFO, "Channel Created " + msChannel.getName());
            Collection peers = msChannel.getPeers();
            Iterator peersIterator = peers.iterator();
            while (peersIterator.hasNext()) {
                Peer peer = (Peer) peersIterator.next();
                Logger.getLogger(CreateChannel.class.getName()).log(Level.INFO, peer.getName() + " at " + peer.getUrl());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
