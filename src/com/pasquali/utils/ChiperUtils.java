package com.pasquali.utils;

import org.apache.commons.lang3.SerializationUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Dario on 18/02/2017.
 */
public class ChiperUtils {

    private static String algorithm = "AES";
    private static Cipher cipher = null;

    static {

        //keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        //FileInputStream fio = new FileInputStream(keyFileName);
        //keyStore.load(fio, password);

        try
        {
            cipher = Cipher.getInstance(algorithm);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
    }

    public static byte[] encrypt(String input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input.getBytes();
        return cipher.doFinal(inputBytes);
    }

    public static byte[] encrypt(byte[] input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input;
        return cipher.doFinal(inputBytes);
    }

    public static byte[] encrypt(Serializable input, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = SerializationUtils.serialize(input);
        return cipher.doFinal(inputBytes);
    }

    public static byte[] decrypt(byte[] encryptionBytes, Key key)
            throws InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
        return recoveredBytes;
    }

}
