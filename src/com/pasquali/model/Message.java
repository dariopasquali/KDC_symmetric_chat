package com.pasquali.model;

import java.io.Serializable;

/**
 * Created by Dario on 18/02/2017.
 */
public class Message implements Serializable{

    public byte[] encSessionKey;
    public byte[] encMessage;

    public Message(byte[] encSessionKey, byte[] encMessage) {
        this.encSessionKey = encSessionKey;
        this.encMessage = encMessage;
    }
}
