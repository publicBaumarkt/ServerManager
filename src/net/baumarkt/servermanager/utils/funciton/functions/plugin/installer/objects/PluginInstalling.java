package net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.objects;

/*
 * Created on 15.08.2020 at 14:05
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.sun.istack.internal.NotNull;
import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.PluginInstallerCategory;

import java.io.IOException;

public class PluginInstalling {

    private final PluginInstallerCategory pluginInstallerCategory;
    private final int id;
    private String name;
    private final String auhor;
    private String description;
    private String totalDownloads;
    private String rating;

    public PluginInstalling(final PluginInstallerCategory pluginInstallerCategory, @NotNull final int id, final String author){
        this.pluginInstallerCategory = pluginInstallerCategory;
        this.id = id;
        this.auhor = author;

        try {
           this.description = ServerManager.getInstance().getPluginInstaller().getSpigotMcPluginInfo(id).get("tag").getAsString();
           this.totalDownloads = ServerManager.getInstance().getPluginInstaller().getSpigotMcPluginInfo(id).get("downloads").getAsString();
           this.rating = "" + ServerManager.getInstance().getPluginInstaller().getSpigotMcPluginInfo(id).get("rating").getAsJsonObject().get("average").getAsInt();
           this.name = ServerManager.getInstance().getPluginInstaller().getSpigotMcPluginInfo(id).get("name").getAsString();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public PluginInstallerCategory getPluginInstallerCategory() {
        return pluginInstallerCategory;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public String getAuhor() {
        return auhor;
    }

    public String getRating() {
        return rating;
    }

    public String getTotalDownloads() {
        return totalDownloads;
    }
}
