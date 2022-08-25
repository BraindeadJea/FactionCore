package com.itndev.EloSystem.API;

import com.itndev.EloSystem.Storage.SQLite;

import java.util.concurrent.CompletableFuture;

public class Loader {

    public static CompletableFuture<Boolean> run() {
        CompletableFuture<Boolean> success = new CompletableFuture<>();
        try {
            SQLite.setup("Elo.db");
            SQLite.open();
            success.complete(true);
        } catch (Exception e) {
            e.printStackTrace();
            success.complete(false);
        }

        return success;
    }
}
