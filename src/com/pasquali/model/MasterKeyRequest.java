package com.pasquali.model;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Created by Dario on 17/02/2017.
 */
public class MasterKeyRequest implements Serializable{

    public byte[] nonce;
    public String idA;
    public String idB;

    public MasterKeyRequest(byte[] nonce, String a, String b) {
        this.nonce = nonce;
        idA = a;
        idB = b;
    }
}
