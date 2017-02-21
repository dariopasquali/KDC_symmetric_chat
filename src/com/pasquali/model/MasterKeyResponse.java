package com.pasquali.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.Key;

/**
 * Created by Dario on 17/02/2017.
 */
public class MasterKeyResponse implements Serializable{

    public byte[] nonce;
    public Key key;
    public String B;

    public byte[] encryptedMKagreement;

    public MasterKeyResponse( byte[] nonce, String b, Key key, byte[] mKagreement) {
        this.nonce = nonce;
        this.key = key;
        B = b;
        this.encryptedMKagreement = mKagreement;
    }
}
