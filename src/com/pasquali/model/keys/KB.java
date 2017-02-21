package com.pasquali.model.keys;

import java.security.Key;

/**
 * Created by Dario on 18/02/2017.
 */
public class KB implements Key {

    private  byte[] key = {-25, 73, 41, 113, -88, -89, -84, -49, -59, -124, 82, 25, -112, -72, -127, 56};

    @Override
    public String getAlgorithm() {
        return "AES";
    }

    /*
    private  byte[] key = {4, -108, 2, 22, 87, 16, 49, -3, -63, 50, -89, -2, -99, -110, -108, 97, 104, -36, 93, 69, 81, -82, 19, 56};

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
