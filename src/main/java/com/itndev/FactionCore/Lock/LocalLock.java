package com.itndev.FactionCore.Lock;

public class LocalLock {


    private final LockObject obj;
    public LocalLock(String key) {
        obj = new LockObject(key);
    }

    public LockObject getLock() {
        return obj;
    }
}
