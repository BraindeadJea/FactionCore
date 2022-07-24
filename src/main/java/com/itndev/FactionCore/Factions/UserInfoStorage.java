package com.itndev.FactionCore.Factions;

import java.util.concurrent.ConcurrentHashMap;

public class UserInfoStorage {

    public static ConcurrentHashMap<String, String> uuidname = new ConcurrentHashMap<>(); // uuid -> lowercase name
    public static ConcurrentHashMap<String, String> nameuuid = new ConcurrentHashMap<>(); // lowercase name -> uuid
    public static ConcurrentHashMap<String, String> namename = new ConcurrentHashMap<>();

    public static void UserInfoStorageUpdateHandler(String[] args) {
        if(args[1].equalsIgnoreCase("namename")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.namename.containsKey(key)) {
                        UserInfoStorage.namename.remove(key);
                        UserInfoStorage.namename.put(key, value);
                    } else {
                        UserInfoStorage.namename.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.namename.containsKey(key)) {

                        UserInfoStorage.namename.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.namename.containsKey(key)) {
                    UserInfoStorage.namename.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("nameuuid")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.nameuuid.containsKey(key)) {
                        UserInfoStorage.nameuuid.remove(key);
                        UserInfoStorage.nameuuid.put(key, value);
                    } else {
                        UserInfoStorage.nameuuid.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.nameuuid.containsKey(key)) {

                        UserInfoStorage.nameuuid.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }

                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.nameuuid.containsKey(key)) {
                    UserInfoStorage.nameuuid.remove(key);
                }
                key = null;
            }

        } else if(args[1].equalsIgnoreCase("uuidname")) {

            if(args[2].equalsIgnoreCase("add")) {
                String key = args[3]; //키
                String value = args[5]; //추가하고 싶은 값

                if(args[4].equalsIgnoreCase("add")) {

                    if(UserInfoStorage.uuidname.containsKey(key)) {
                        UserInfoStorage.uuidname.remove(key);
                        UserInfoStorage.uuidname.put(key, value);
                    } else {
                        UserInfoStorage.uuidname.put(key, value);
                    }
                } else if(args[4].equalsIgnoreCase("remove")) {

                    if(UserInfoStorage.uuidname.containsKey(key)) {

                        UserInfoStorage.uuidname.remove(key);

                        //할거 없다
                    } else {
                        //할거 없다
                    }
                }
                key = null;
                value = null;

            } else if(args[2].equalsIgnoreCase("remove")) {
                String key = args[3];
                if(UserInfoStorage.uuidname.containsKey(key)) {
                    UserInfoStorage.uuidname.remove(key);
                }
                key = null;
            }

        }
    }

}
