package com.pasquali.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.Key;

/**
 * Created by Dario on 17/02/2017.
 */
public class MKagreement implements Serializable {

    public String A;
    public Key key;

    public MKagreement(String a, Key key) {
        A = a;
        this.key = key;
    }
}
