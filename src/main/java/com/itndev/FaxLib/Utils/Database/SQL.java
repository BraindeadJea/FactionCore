package com.itndev.FaxLib.Utils.Database;

public class SQL {

    private String address;
    private int port;
    private char[] password;
    private boolean useSSL;

    public SQL(String address, int port, char[] password, boolean useSSL) {
        this.address = address;
        this.port = port;
        this.password = password;
        this.useSSL = useSSL;
    }
}
