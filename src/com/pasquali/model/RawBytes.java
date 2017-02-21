package com.pasquali.model;

import java.io.Serializable;

/**
 * Created by Dario on 18/02/2017.
 */
public class RawBytes implements Serializable{

    public byte[] rawData;

    public RawBytes(byte[] rawData) {
        this.rawData = rawData;
    }
}
