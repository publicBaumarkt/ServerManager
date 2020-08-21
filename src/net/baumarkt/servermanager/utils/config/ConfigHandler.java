package net.baumarkt.servermanager.utils.config;

/*
 * Created on 16.06.2020 at 14:25
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.baumarkt.servermanager.ServerManager;
import org.bukkit.ChatColor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class ConfigHandler {

    private JsonObject jsonObject;

    public ConfigHandler(){
        new File("plugins/ServerManager/").mkdir();

        Path path = Paths.get("plugins/ServerManager/", "config.json");
        File file = new File("plugins/ServerManager/config.json");

        if(Files.notExists(path) && !file.exists()){
            try {
                Files.copy(ServerManager.class.getClassLoader().getResourceAsStream("config.json"), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject = ServerManager.getInstance().getGson().fromJson(new FileReader("plugins/ServerManager/config.json"), JsonObject.class);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void set(final String path, final JsonElement value){
        jsonObject.remove(path);
        jsonObject.add(path, value);
    }

    public JsonObject getJsonObject(final String parameter){
        return jsonObject.getAsJsonObject(parameter);
    }

    public String getContent(final String path, Object... replace){
        final String rawString = jsonObject.get(path).getAsString();
        byte[] bytes = rawString.getBytes(StandardCharsets.UTF_8);
        final String result = new String(bytes, StandardCharsets.UTF_8);

        return ChatColor.translateAlternateColorCodes('&', MessageFormat.format(result, replace));
    }

    public Boolean getBooleanContent(final String path){
        return jsonObject.get(path).getAsBoolean();
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }
}
