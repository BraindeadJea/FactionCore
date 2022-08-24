package com.itndev.FactionCore.SocketConnection;

public class kurumi {

    public static Integer Kurumi(int number) {
        int cache = 1;
        int cache2 = 1;
        for(int x = 1; x <= number; x++) {
            if(x > 2) {
                int cache3 = cache2;
                cache2 = cache;
                cache = cache + cache3;
            }
        }
        return cache;
    }
}
