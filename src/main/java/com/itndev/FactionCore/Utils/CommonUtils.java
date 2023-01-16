package com.itndev.FactionCore.Utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CommonUtils {

    public static int toInt(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static byte[] decode(String s, Charset charset) {
        return s.getBytes(charset);
    }

    public static String encode(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    public static String String2Byte(String s) {
        return Arrays.toString(decode(s, StandardCharsets.UTF_16)).replace(" ", "");
    }

    public static String Byte2String(String bytes) {
        if(bytes.contains(",")) {
            String[] computed = bytes.replace("[", "").replace("]", "").split(",");
            byte[] bytes1 = new byte[computed.length];
            int amount = 0;
            for (String byte_ : computed) {
                bytes1[amount] = Byte.parseByte(byte_);
                amount++;
            }
            return encode(bytes1, StandardCharsets.UTF_16);
        } else {
            String computed = bytes.replace("[", "").replace("]", "");
            byte[] bytes1 = new byte[1];
            bytes1[0] = Byte.parseByte(computed);
            return encode(bytes1, StandardCharsets.UTF_16);
        }
    }
}
