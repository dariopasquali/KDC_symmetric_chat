package com.pasquali.model.keys;

import java.security.Key;

/**
 * Created by Dario on 18/02/2017.
 */
public class KA implements Key {


    private byte[] key = {-25, 38, 122, 6, -101, 74, -2, 42, -41, 54, 43, 102, -113, 3, -34, -43};

    @Override
    public String getAlgorithm() {
        return "AES";
    }


    /*
    private byte[] key = {-60, -89, 104, -9, -68, 52, -14, 31, -108, -27, 82, 98, -2, -74, -60, -48, 4, -9, -60, 22, -9, 104, -110, 112};

    @Override
    public String getAlgorithm() {
        return "DESede";
    }
    */

    @Override
    public String getFormat() {
        return "RAW";
    }

    @Override
    public byte[] getEncoded() {
        return key;
    }
}
