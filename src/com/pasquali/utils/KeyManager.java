package com.pasquali.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.HashMap;

/**
 * Created by Dario on 17/02/2017.
 */
public class KeyManager{


    //private static KeyStore keyStore = null;
    //private static char[] password = new String("password").toCharArray();
    //private static String keyFileName = "my.keystore";

    private static String algorithm = "AES";

    private static HashMap<String, Key> keystore = null;

    static{
        try
        {
            keystore = new HashMap<>();
            Key ka = generateKey();
            Key kb = generateKey();

            keystore.put("Alice", ka);
            keystore.put("bob", kb);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Key generateKey() throws Exception{

        SecretKey k =  KeyGenerator.getInstance(algorithm).generateKey();
        return new SecretKeySpec(k.getEncoded(), 0, 16, algorithm);
    }

    public static Key lookup(String alias)
    {
        return (keystore!=null) ? keystore.get(alias) : null;
    }

}
