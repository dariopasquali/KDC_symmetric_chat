package com.pasquali.test;

import com.pasquali.utils.ChiperUtils;
import com.pasquali.utils.KeyManager;

import java.math.BigInteger;
import java.security.Key;

/**
 * Created by Dario on 18/02/2017.
 */
public class EDtest {


    public static void main(String[] args) throws Exception {

        String test = "ciaone!!";

        Key k = KeyManager.generateKey();

        for(byte b : k.getEncoded())
            System.out.print(b+", ");
        System.out.println(k.getAlgorithm()+" "+k.getFormat());

        System.out.println("\n");

        System.out.println();
        byte[] enc = ChiperUtils.encrypt(test, k);

        for(byte b : enc)
           System.out.print(b+", ");

        String dec = new String(ChiperUtils.decrypt(enc, k));

        System.out.println(dec);


        BigInteger big = new BigInteger(String.valueOf(12345));
        for(byte b : big.toByteArray())
            System.out.print(b+", ");

        enc = ChiperUtils.encrypt(big.toByteArray(), k);
        for(byte b : enc)
            System.out.print(b+", ");

        BigInteger bigDec = new BigInteger(ChiperUtils.decrypt(enc, k));
        System.out.println(bigDec.toString());


        byte[] KA = {-60, -89, 104, -9, -68, 52, -14, 31, -108, -27, 82, 98, -2, -74, -60, -48, 4, -9, -60, 22, -9, 104, -110, 112};
        byte[] KB = {4, -108, 2, 22, 87, 16, 49, -3, -63, 50, -89, -2, -99, -110, -108, 97, 104, -36, 93, 69, 81, -82, 19, 56};






    }

}
