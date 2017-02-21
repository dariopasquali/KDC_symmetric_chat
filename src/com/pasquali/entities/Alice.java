package com.pasquali.entities;

import com.pasquali.model.MasterKeyResponse;
import com.pasquali.model.Message;
import com.pasquali.model.RawBytes;
import com.pasquali.model.keys.KA;
import com.pasquali.utils.ChiperUtils;
import com.pasquali.model.MasterKeyRequest;
import com.pasquali.utils.KeyManager;
import com.pasquali.utils.NumberUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.*;
import java.math.BigInteger;
import java.net.Socket;
import java.security.*;
import java.util.Arrays;

/**
 * Created by Dario on 17/02/2017.
 */
public class Alice {

    static String kdcAddr = "localhost";
    static int kdcPort = 5555;
    static ObjectOutputStream outKdcSock = null;
    static ObjectInputStream inKdcSock = null;

    static String bobAddr = "localhost";
    static int bobPort = 6666;
    static ObjectOutputStream outBobSock = null;
    static ObjectInputStream inBobSock = null;

    private static String aliceID = "Alice";
    private static String bobID = "Bob";

    private static Key masterKey;
    private static KA ka;

    public static void main(String[] args)
    {
        Socket kdc = null,
                bob = null;

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));


        byte[] RaNonce = NumberUtils.getNonce();

        try
        {

            // **************** INIT *******************

            kdc = new Socket(kdcAddr, kdcPort);
            System.out.println("ALICE || Socket connected to KDC");

            outKdcSock = new ObjectOutputStream(kdc.getOutputStream());
            inKdcSock = new ObjectInputStream(kdc.getInputStream());

            // **************** DEFINE KA *******************
            /*
            Per semplicità suppongo che Alice e KDC
            abbiano già precondiviso una chaive simmetrica KA
            che il KDC si è preoccupato di salvare in una locazione sicura
             */

            ka = new KA();

            //***************** MASTER KEY REQUEST ******************************

            System.out.print("ALICE || Starting KDC master key request");

            MasterKeyRequest mkReq = new MasterKeyRequest(RaNonce, aliceID, bobID);
            outKdcSock.writeObject(mkReq);
            outKdcSock.flush();
            System.out.println("ALICE || Master Key Request sent, i'm waiting the KDC answer");

            //***************** MASTER KEY RESPONSE ******************************

            RawBytes raw = (RawBytes) inKdcSock.readObject();
            byte[] decrypted = ChiperUtils.decrypt(raw.rawData, ka);
            MasterKeyResponse kdcResponse = SerializationUtils.deserialize(decrypted);

            if(!bobID.equals(kdcResponse.B))
            {
                System.out.println("ALICE || The BOB ID is incorrect");
                outKdcSock.close();
                inKdcSock.close();
                kdc.close();
                System.exit(0);
            }

            if(!Arrays.equals(RaNonce, kdcResponse.nonce))
            {
                System.out.println("ALICE || My Ra Nonce is incorrect");
                outKdcSock.close();
                inKdcSock.close();
                kdc.close();
                System.exit(0);
            }

            masterKey = kdcResponse.key;
            RawBytes encryptedMKtoBob = new RawBytes(kdcResponse.encryptedMKagreement);

            //======================================================================================
            //***************** CLOSE KDC SOCKET AND OPEN BOB SOCKET ******************************
            //======================================================================================

            outKdcSock.close();
            inKdcSock.close();
            kdc.close();

            bob = new Socket(bobAddr, bobPort);
            System.out.println("ALICE || Socket connected to BOB");

            outBobSock = new ObjectOutputStream(bob.getOutputStream());
            inBobSock = new ObjectInputStream(bob.getInputStream());

            //***************** SEND MASTER KEY TO BOB ******************************

            outBobSock.writeObject(encryptedMKtoBob);
            outBobSock.flush();
            System.out.println("ALICE || Alice Sent Master key to Bob");

            //***************** RECEIVE CHALLENGE FROM BOB ******************************

            RawBytes challenge = (RawBytes) inBobSock.readObject();
            BigInteger RbNonce = new BigInteger(ChiperUtils.decrypt(challenge.rawData, masterKey));

            //***************** SEND RESPONSE BOB ******************************

            BigInteger response = RbNonce.subtract(new BigInteger(String.valueOf(1)));
            RawBytes data = new RawBytes(ChiperUtils.encrypt(response.toByteArray(), masterKey));
            outBobSock.writeObject(data);

            System.out.println("ALICE || It works!!");
            System.out.println("ALICE || Now we can chat with the Master Key Protocol!");

            while (true)
            {
                System.out.println("Message: ");
                String msg = in.readLine();

                Key sessionKey = KeyManager.generateKey();

                byte[] encKey = ChiperUtils.encrypt(sessionKey, masterKey);
                byte[] encMessage =ChiperUtils.encrypt(msg, sessionKey);

                Message message = new Message(encKey, encMessage);

                outBobSock.writeObject(message);

                System.out.println("-------------------------Message sent");
                System.out.println("-------------------------Wait Bob Answer");

                Message answer = (Message) inBobSock.readObject();

                Key bobSessionKey =
                        SerializationUtils.deserialize(ChiperUtils.decrypt(answer.encSessionKey, masterKey));

                String ansString = new String(ChiperUtils.decrypt(answer.encMessage, bobSessionKey));

                System.out.println("Answer: "+ansString);
            }


        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


}
