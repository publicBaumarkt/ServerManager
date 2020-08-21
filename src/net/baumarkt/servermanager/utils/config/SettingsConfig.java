package net.baumarkt.servermanager.utils.config;

/*
 * Created on 13.08.2020 at 20:58
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import net.baumarkt.servermanager.ServerManager;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsConfig {

    private final YamlConfiguration configuration;
    private final File file;

    public SettingsConfig(){
        new File("plugins/ServerManager/").mkdir();

        Path path = Paths.get("plugins/ServerManager/", "settings.yml");
        file = new File("plugins/ServerManager/settings.yml");

        if(Files.notExists(path) && !file.exists()){
            try {
                Files.copy(ServerManager.class.getClassLoader().getResourceAsStream("settings.yml"), path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = YamlConfiguration.loadConfiguration(file);
    }

    public void saveConfig(){
        try {
            configuration.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public YamlConfiguration getConfiguration() {
        return configuration;
    }
}
