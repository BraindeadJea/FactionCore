package com.itndev.FaxLib.Utils.File;

import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class Yaml {

    public static void CreateFile(String FileName) {
        try {
            File file = new File(FileName);
            if(!file.exists() || !file.isDirectory()) {
                file.mkdir();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object LoadYaml(String filename) {
        try {
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml();
            FileReader reader = new FileReader(filename + ".yml");
            return yaml.load(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("[ERROR] Cant Load Data");
        return null;
    }

    public static void DumpYaml(Object data, String filename) {
        try {
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml(new Constructor(data.getClass()));
            FileWriter writer;
            writer = new FileWriter(filename + ".yml");
            yaml.dump(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
