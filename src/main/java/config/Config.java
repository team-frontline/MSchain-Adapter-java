package config;

import java.io.File;

public class Config {

    public static final String ORG1_MSP = "Org1MSP";

    public static final String ORG2_MSP = "Org2MSP";

    public static final String ADMIN = "admin";

    public static final String BASE_PATH = "/home/supimi/poc/MSchain-Adapter-java/src/network_resources";

    public static final String ORDERER_URL = "grpc://localhost:7050";

    public static final String ORDERER_NAME = "orderer.example.com";

    public static final String CHANNEL_CONFIG_PATH = BASE_PATH + File.separator + "config/channel.tx";

    public static final String CHANNEL_NAME = "mychannel";

    public static final String ORG1_PEER_0 = "peer0.org1.example.com";

    public static final String ORG1_PEER_0_URL = "grpc://localhost:7051";

    public static final String ORG1_PEER_1 = "peer1.org1.example.com";

    public static final String ORG1_PEER_1_URL = "grpc://localhost:7056";

    public static final String ORG2_PEER_0 = "peer0.org2.example.com";

    public static final String ORG2_PEER_0_URL = "grpc://localhost:8051";

    public static final String ORG2_PEER_1 = "peer1.org2.example.com";

    public static final String ORG2_PEER_1_URL = "grpc://localhost:8056";


    public static final String ORG1_ADMIN_PK_PATH = BASE_PATH + File.separator + "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org1.example.com" + File.separator + "users" + File.separator + "Admin@org1.example.com"
            + File.separator + "msp" + File.separator + "keystore";

    public static final String ORG1_ADMIN_CERT_PATH = BASE_PATH + File.separator + "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org1.example.com" + File.separator + "users" + File.separator + "Admin@org1.example.com"
            + File.separator + "msp" + File.separator + "admincerts";

    public static final String ORG2_ADMIN_PK_PATH = BASE_PATH + File.separator + "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org2.example.com" + File.separator + "users" + File.separator + "Admin@org2.example.com"
            + File.separator + "msp" + File.separator + "keystore";

    public static final String ORG2_ADMIN_CERT_PATH = BASE_PATH + File.separator + "crypto-config" + File.separator + "peerOrganizations" + File.separator
            + "org2.example.com" + File.separator + "users" + File.separator + "Admin@org2.example.com"
            + File.separator + "msp" + File.separator + "admincerts";

    public static final String CHAINCODE_ROOT_DIR = "chaincode";

    public static final String CHAINCODE_NAME = "fabcar";

    public static final String CHAINCODE_PATH = "github.com/fabcar";

    public static final String CHAINCODE_VERSION = "1";

}
