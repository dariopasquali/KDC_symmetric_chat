package com.pasquali.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Dario on 18/02/2017.
 */
public class NumberUtils {

    public static BigInteger expMod(BigInteger base, BigInteger exp, BigInteger modulo)
    {
        BigInteger result = BigInteger.ONE;
        while (exp.compareTo(BigInteger.ZERO) > 0) {
            if (exp.testBit(0)) // then exponent is odd
                result = (result.multiply(base)).mod(modulo);
            exp = exp.shiftRight(1);
            base = (base.multiply(base)).mod(modulo);
        }
        return result.mod(modulo);
    }

    public static byte[] getNonce()
    {
        try
        {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            byte[] seed = new byte[128];
            random.nextBytes(seed);
            return seed;

        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}
