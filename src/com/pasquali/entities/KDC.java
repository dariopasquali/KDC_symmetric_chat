package com.pasquali.entities;

import com.pasquali.model.MKagreement;
import com.pasquali.model.MasterKeyRequest;
import com.pasquali.model.MasterKeyResponse;
import com.pasquali.model.RawBytes;
import com.pasquali.model.keys.KA;
import com.pasquali.model.keys.KB;
import com.pasquali.utils.ChiperUtils;
import com.pasquali.utils.KeyManager;
import com.pasquali.utils.NumberUtils;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;

/**
 * Created by Dario on 18/02/2017.
 */
public class KDC {

    static ObjectOutputStream outSock = null;
    static ObjectInputStream inSock = null;

    private static String aliceID = "Alice";
    private static String bobID = "Bob";

    private static Key masterKey;
    private static KA ka;
    private static KB kb;


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
            ka = new KA();
            kb = new KB();

            // **************** WAIT ALICE'S CONNECTION *******************

            ServerSocket socket = new ServerSocket(5555);
            System.out.println("KDC in ascolto, porta 5555\n");

            Socket alice = socket.accept();
            System.out.println("client connesso");

            inSock = new ObjectInputStream(alice.getInputStream());
            outSock = new ObjectOutputStream(alice.getOutputStream());

            // **************** WAIT MASTER KEY REQUEST *******************

            MasterKeyRequest request = (MasterKeyRequest)inSock.readObject();

            System.out.println("ID:"+request.idA+" richeide una master key");
            System.out.println("per comunicare con ID:"+request.idB);
            System.out.println("Estraggo le chiavi simmetriche KA e KB");

            // **************** GENERATE MASTER KEY AND ANSWER *******************

            System.out.println("Genero la chiave di sessione");
            masterKey = KeyManager.generateKey();

            MKagreement agreement = new MKagreement(aliceID, masterKey);
            byte[] encryptedAgreement = ChiperUtils.encrypt(agreement, kb);

            MasterKeyResponse response =
                    new MasterKeyResponse(request.nonce, bobID, masterKey, encryptedAgreement);

            byte[] encResponse = ChiperUtils.encrypt(response, ka);

            outSock.writeObject(new RawBytes(encResponse));

            System.out.println("Chiave di sessione inviata, termino il protocollo.");

            inSock.close();
            outSock.close();
            alice.close();
            socket.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
