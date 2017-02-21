package com.pasquali.test;

import com.pasquali.model.MKagreement;
import com.pasquali.model.MasterKeyResponse;
import com.pasquali.model.keys.KA;
import com.pasquali.model.keys.KB;
import com.pasquali.utils.ChiperUtils;
import com.pasquali.utils.KeyManager;
import com.pasquali.utils.NumberUtils;
import org.apache.commons.lang3.SerializationUtils;


/**
 * Created by Dario on 18/02/2017.
 */
public class EDSerializbleObject {

    public static void main(String[] args)
    {
        try
        {
            MKagreement agreement = new MKagreement("alice", KeyManager.generateKey());

            byte[] enc = ChiperUtils.encrypt(agreement, new KB());
            byte[] dec = ChiperUtils.decrypt(enc, new KB());

            MKagreement decAgr = SerializationUtils.deserialize(dec);

            System.out.println(decAgr.A+" "+decAgr.key);





            MasterKeyResponse response =
                    new MasterKeyResponse(NumberUtils.getNonce(), "BoB", KeyManager.generateKey(), enc);

            byte[] encR = ChiperUtils.encrypt(response, new KA());
            byte[] decR = ChiperUtils.decrypt(encR, new KA());

            MasterKeyResponse decResponse = SerializationUtils.deserialize(decR);

            System.out.println(decResponse.B+" "+decResponse.key);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
