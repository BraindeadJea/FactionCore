package com.itndev.FBTM.Database.Redis.Obj;

public class StreamConfig {

    private static String Stream_INPUT_NAME = "INPUT_FRONTEND_TO_BACKEND";
    private static String Stream_OUTPUT_NAME = "OUTPUT_BACKEND_TO_FRONTEND";
    private static String Stream_INTERCONNECT_NAME = "INTERCONNECT_INNER";

    public static String get_Stream_INPUT_NAME() {
        return Stream_INPUT_NAME;
    }

    public static String get_Stream_OUTPUT_NAME() {
        return Stream_OUTPUT_NAME;
    }

    public static String get_Stream_INTERCONNECT_NAME() {
        return Stream_INTERCONNECT_NAME;
    }
}
