package com.pasquali.entities;

import com.pasquali.model.MKagreement;
import com.pasquali.model.Message;
import com.pasquali.model.RawBytes;
import com.pasquali.model.keys.KA;
import com.pasquali.model.keys.KB;
import com.pasquali.utils.ChiperUtils;
import com.pasquali.utils.KeyManager;
import com.pasquali.utils.NumberUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

/**
 * Created by Dario on 18/02/2017.
 */
public class Bob {

    static ObjectOutputStream outSock = null;
    static ObjectInputStream inSock = null;

    private static String bobID = "Bob";

    private static Key masterKey;
    private static KB kb;

    private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args)
    {
        try
        {
            // **************** INIT *******************

            /*
            suppongo che Alice e Bob abbiano precondiviso con
            il KDC una chiave simmetrica segreta
            e che il KDC le abbia salvate in maniera sicura
             */

            kb = new KB();

            // **************** WAIT ALICE'S CONNECTION *******************

            ServerSocket socket = new ServerSocket(6666);
            System.out.println("BOB || Bob listening on 6666\n");

            Socket alice = socket.accept();
            System.out.println("BOB || Hi Alice!!");

            inSock = new ObjectInputStream(alice.getInputStream());
            outSock = new ObjectOutputStream(alice.getOutputStream());

            // **************** WAIT MASTER KEY AGREEMENT *******************

            RawBytes encryptedAgreement = (RawBytes) inSock.readObject();
            MKagreement mkAgreement = SerializationUtils
                    .deserialize(ChiperUtils.decrypt(encryptedAgreement.rawData, kb));

            System.out.println("BOB || Receive Master Key Agreement");
            System.out.println("BOB || from "+mkAgreement.A);
            System.out.println("BOB || master key "+ mkAgreement.key);

            masterKey = mkAgreement.key;

            // **************** SEND CHALLENGE *******************

            System.out.println("BOB || let's check the identity of Alice");

            byte[] Rb = NumberUtils.getNonce();
            outSock.writeObject(new RawBytes(ChiperUtils.encrypt(Rb, masterKey)));
            outSock.flush();

            // **************** WAIT CHALLENGE RESPONSE *******************

            BigInteger bigNonce = new BigInteger(Rb);

            System.out.println("BOB || I sent "+bigNonce.toString());

            BigInteger challengeCheck = bigNonce.subtract(new BigInteger(String.valueOf(1)));

            System.out.println("BOB || I expect "+challengeCheck.toString());

            RawBytes encryptAnswer = (RawBytes) inSock.readObject();
            BigInteger answer = new BigInteger(ChiperUtils.decrypt(encryptAnswer.rawData, masterKey));

            System.out.println("BOB || I received "+answer.toString());

            if(answer.equals(challengeCheck))
            {
                System.out.println("BOB || Alice confirmed my challenge, she obtain the master key from the KDC");

                System.out.println("BOB || It works!!");
                System.out.println("BOB || Now we can chat with the Master Key Protocol!");

                while (true)
                {
                    Message ansMsg = (Message) inSock.readObject();

                    Key bobSessionKey =
                            SerializationUtils.deserialize(ChiperUtils.decrypt(ansMsg.encSessionKey, masterKey));

                    String ansString = new String(ChiperUtils.decrypt(ansMsg.encMessage, bobSessionKey));

                    System.out.println("Alice Message: "+ansString);

                    System.out.println("Answer: ");
                    String msg = in.readLine();

                    Key sessionKey = KeyManager.generateKey();

                    byte[] encKey = ChiperUtils.encrypt(sessionKey, masterKey);
                    byte[] encMessage =ChiperUtils.encrypt(msg, sessionKey);

                    Message message = new Message(encKey, encMessage);

                    outSock.writeObject(message);

                    System.out.println("-------------------------Answer sent");
                    System.out.println("-------------------------Wait Alice Message");
                }
            }
            else
            {
                System.out.print("BOB || Alice è una perzona falza!!!!! PULUZIA CONTATTI IS COMING");
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


}
