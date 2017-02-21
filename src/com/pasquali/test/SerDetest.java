package com.pasquali.test;

import com.pasquali.model.MKagreement;
import com.pasquali.utils.KeyManager;
import org.apache.commons.lang3.SerializationUtils;

/**
 * Created by Dario on 18/02/2017.
 */
public class SerDetest {

    public static void main(String[] args) throws Exception
    {
        MKagreement agreement = new MKagreement("alice", KeyManager.generateKey());

        byte[] ser = SerializationUtils.serialize(agreement);

        MKagreement des = SerializationUtils.deserialize(ser);

        System.out.println(des.A);
        System.out.println(des.key.getEncoded());
    }

}
