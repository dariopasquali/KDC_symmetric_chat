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
            System.out.println("Bob in ascolto, porta 6666\n");

            Socket alice = socket.accept();
            System.out.println("Client connesso");

            inSock = new ObjectInputStream(alice.getInputStream());
            outSock = new ObjectOutputStream(alice.getOutputStream());

            // **************** WAIT MASTER KEY AGREEMENT *******************

            RawBytes encryptedAgreement = (RawBytes) inSock.readObject();
            MKagreement mkAgreement = SerializationUtils
                    .deserialize(ChiperUtils.decrypt(encryptedAgreement.rawData, kb));

            System.out.println("Ricevo la master key");
            System.out.println("Da ID:"+mkAgreement.A);

            masterKey = mkAgreement.key;

            // **************** SEND CHALLENGE *******************

            System.out.println("Verifico l'identità di Alice con un nonce");
            System.out.println("genero Rb e lo trasmetto cifrato con k :: Ek( Rb ) ::");
            System.out.println("Mi aspetto Rb - 1");

            byte[] Rb = NumberUtils.getNonce();
            outSock.writeObject(new RawBytes(ChiperUtils.encrypt(Rb, masterKey)));
            outSock.flush();

            // **************** WAIT CHALLENGE RESPONSE *******************

            BigInteger bigNonce = new BigInteger(Rb);
            BigInteger challengeCheck = bigNonce.subtract(new BigInteger(String.valueOf(1)));

            RawBytes encryptAnswer = (RawBytes) inSock.readObject();
            BigInteger answer = new BigInteger(ChiperUtils.decrypt(encryptAnswer.rawData, masterKey));

            if(answer.equals(challengeCheck))
            {
                System.out.println("La risposta di Alice è corretta, ha ottenuto la Master Key dal KDC");
                System.out.println("Inoltre la chiave non è mai stata usata prima");
                System.out.println("ora possiamo chattare");

                while (true)
                {
                    Message ansMsg = (Message) inSock.readObject();

                    Key bobSessionKey =
                            SerializationUtils.deserialize(ChiperUtils.decrypt(ansMsg.encSessionKey, masterKey));

                    String ansString = new String(ChiperUtils.decrypt(ansMsg.encMessage, bobSessionKey));

                    System.out.println("Messaggio di Alice: "+ansString);

                    System.out.print("Risposta: ");
                    String msg = in.readLine();

                    Key sessionKey = KeyManager.generateKey();

                    byte[] encKey = ChiperUtils.encrypt(sessionKey, masterKey);
                    byte[] encMessage =ChiperUtils.encrypt(msg, sessionKey);

                    Message message = new Message(encKey, encMessage);

                    outSock.writeObject(message);

                    System.out.println("::: Risposta inviata :::");
                    System.out.println("::: Attendo messaggio :::");
                }
            }
            else
            {
                System.out.print("Il nonce non corrisponde, o la chiave è già stata usata");
                System.out.print("termino il protocollo");
            }



        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


}
