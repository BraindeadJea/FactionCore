package com.itndev.FaxLib.Utils.Data;

import java.util.List;

public class DataStream {

    private List<String> Stream;
    private String ServerName;
    private String DataType;

    private Boolean isEmpty = false;

    public DataStream(String ServerName, String DataType, List<String> Stream) {
        this.ServerName = ServerName;
        this.DataType = DataType;
        this.Stream = Stream;
        this.isEmpty = false;
    }

    public DataStream() {
        this.ServerName = null;
        this.DataType = null;
        this.Stream = null;
        this.isEmpty = true;
    }

    public Object get(int data) {
        switch (data) {
            case -1:
                return ServerName;
            case -2:
                return DataType;
            case 1:
                return Stream;
            default:
                return null;
        }
    }

    public Boolean isEmpty() {
        return this.isEmpty;
    }

    public String getServerName() {
        return this.ServerName;
    }

    public String getDataType() {
        return this.DataType;
    }

    public List<String> getStream() {
        return this.Stream;
    }
}
