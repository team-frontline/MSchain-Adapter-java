package config;

import java.io.File;

public class Config {

    public static final String ORG1_MSP = "Org1MSP";

    public static final String ORG2_MSP = "Org2MSP";

    public static final String ADMIN = "admin";

    public static final String ORDERER_URL = "grpc://localhost:7050";

    public static final String ORDERER_NAME = "orderer.example.com";

    public static final String CHANNEL_CONFIG_PATH = "config/channel.tx";

    public static final String CHANNEL_NAME = "MSchannel";

    public static final String ORG1_PEER_0 = "peer0.org1.example.com";

    public static final String ORG1_PEER_0_URL = "grpc://localhost:7051";

    public static final String ORG1_PEER_1 = "peer1.org1.example.com";

    public static final String ORG1_PEER_1_URL = "grpc://localhost:7056";

    public static final String ORG2_PEER_0 = "peer0.org2.example.com";

    public static final String ORG2_PEER_0_URL = "grpc://localhost:8051";

    public static final String ORG2_PEER_1 = "peer1.org2.example.com";

    public static final String ORG2_PEER_1_URL = "grpc://localhost:8056";

    public static final String ORG1_ADMIN_PK_PATH =  "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org1.example.com" + File.separator + "users" + File.separator + "Admin@org1.example.com"
            + File.separator + "msp"+ File.separator + "keystore";

    public static final String ORG1_ADMIN_CERT_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org1.example.com" + File.separator + "users" + File.separator + "Admin@org1.example.com"
            + File.separator + "msp"+ File.separator + "certificates";

    public static final String ORG2_ADMIN_PK_PATH =  "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org2.example.com" + File.separator + "users" + File.separator + "Admin@org2.example.com"
            + File.separator + "msp"+ File.separator + "keystore";

    public static final String ORG2_ADMIN_CERT_PATH = "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org2.example.com" + File.separator + "users" + File.separator + "Admin@org2.example.com"
            + File.separator + "msp"+ File.separator + "certificates";

}
