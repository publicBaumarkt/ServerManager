package net.baumarkt.servermanager.utils.funciton.functions.plugin.installer;

/*
 * Created on 15.08.2020 at 14:03
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.baumarkt.servermanager.utils.funciton.functions.plugin.installer.objects.PluginInstalling;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PluginInstaller {

    private final JsonParser jsonParser = new JsonParser();

    private final List<PluginInstalling> pluginInstallingList = Lists.newArrayList();

    public boolean downloadSpigotMcPlugin(final int pluginId, final File file) throws IOException {
        HttpsURLConnection.setFollowRedirects(true);
        URL apiUrl = new URL("http://aqua.api.spiget.org/v2/resources/" + pluginId + "/download");
        HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();
        connection.setDoInput(true);

        if(connection.getResponseCode() != 200) {
            return false;
        }

        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while((length = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        fos.close();
        bis.close();
        return true;
    }


    public JsonObject getSpigotMcPluginInfo(final int pluginId) throws IOException {
        URL apiUrl = new URL("https://api.spiget.org/v2/resources/" + pluginId);
        HttpsURLConnection connection = (HttpsURLConnection) apiUrl.openConnection();
        connection.setDoInput(true);
        connection.setRequestProperty("user-agent", "Labrix");

        if(connection.getResponseCode() != 200) {
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseBuilder = new StringBuilder();
        String line;
        while((line = br.readLine()) != null) {
            responseBuilder.append(line);
        }
        br.close();

        return jsonParser.parse(responseBuilder.toString()).getAsJsonObject();
    }

    public List<PluginInstalling> getPluginInstallingList() {
        return pluginInstallingList;
    }
}
