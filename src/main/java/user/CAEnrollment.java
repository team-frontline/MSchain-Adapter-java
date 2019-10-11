package user;

import org.hyperledger.fabric.sdk.Enrollment;

import java.io.Serializable;
import java.security.PrivateKey;

public class CAEnrollment implements Enrollment, Serializable {

    private PrivateKey privateKey;
    private String certificate;

    public CAEnrollment(PrivateKey privateKey, String certificate) {
        this.privateKey = privateKey;
        this.certificate = certificate;
    }

    public PrivateKey getKey() {
        return privateKey;
    }

    public String getCert() {
        return certificate;
    }
}
