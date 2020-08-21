package net.baumarkt.servermanager.api.permission;

/*
 * Created on 16.08.2020 at 10:45
 * Copyright by M.
 * You are not allowed to decompile this file!
 */

import com.google.common.collect.Lists;
import net.baumarkt.servermanager.ServerManager;
import net.baumarkt.servermanager.api.events.permission.player.PlayerCreateEvent;
import net.baumarkt.servermanager.api.permission.objects.Group;
import net.baumarkt.servermanager.api.permission.objects.PlayerGroup;
import net.baumarkt.servermanager.utils.GameProfileBuilder;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class PlayerPermissionHandler {

    private final PermissionHandler permissionHandler;

    private final List<String> playersUUID;
    private final List<PlayerGroup> players;

    public PlayerPermissionHandler(final PermissionHandler permissionHandler){
        this.permissionHandler = permissionHandler;
        this.players = Lists.newArrayList();
        playersUUID = Lists.newArrayList();

        if(permissionHandler.getConfiguration().getStringList("players") == null)
            permissionHandler.getConfiguration().set("players", playersUUID);
        else
            playersUUID.addAll(permissionHandler.getConfiguration().getStringList("players"));

        permissionHandler.saveConfig();

        playersUUID.forEach(player ->{
            try {
                final PlayerGroup playerGroup = new PlayerGroup(GameProfileBuilder.fetch(UUID.fromString(player)).getName(), UUID.fromString(player));

                if(!players.contains(playerGroup))
                    players.add(playerGroup);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        });
    }

    public void groupPlayerByUUID(final UUID uuid, final Consumer<PlayerGroup> consumer){
        for(PlayerGroup playerGroup : players){
            if(playerGroup.getUuid().equals(uuid)){
                consumer.accept(playerGroup);
                return;
            }
        }

        consumer.accept(null);
    }

    public void groupPlayerByName(final String name, final Consumer<PlayerGroup> consumer){
        for(PlayerGroup playerGroup : players){
            if(playerGroup.getPlayerName().equals(name)){
                consumer.accept(playerGroup);
                return;
            }
        }

        consumer.accept(null);
    }

    public void createPlayer(final UUID uuid){
        playerExists(uuid, exits -> {
            if(!exits){
                Bukkit.getPluginManager().callEvent(new PlayerCreateEvent(Bukkit.getPlayer(uuid)));

                try {
                    final PlayerGroup playerGroup = new PlayerGroup(GameProfileBuilder.fetch(uuid).getName(), uuid);

                    playersUUID.add(uuid.toString());
                    players.add(playerGroup);

                    permissionHandler.getConfiguration().set("players", playersUUID);
                    permissionHandler.saveConfig();

                    for(Group group : ServerManager.getInstance().getPermissionHandler().getPlayerGroupHandler().getGroups()){
                        if(group.isDefaultGroup())
                            ServerManager.getInstance().getPermissionHandler().getPlayerAPI(uuid).addGroup(group.getName());
                    }

                    createPlayer(playerGroup);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void update(final PlayerGroup playerGroup){
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".name", playerGroup.getPlayerName());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".permission", playerGroup.getPermissions());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".groups", playerGroup.getPlayerGroups());

        permissionHandler.saveConfig();
    }

    private void createPlayer(final PlayerGroup playerGroup){
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".name", playerGroup.getPlayerName());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".permission", playerGroup.getPermissions());
        permissionHandler.getConfiguration().set("payerData." + playerGroup.getUuid()+ ".groups", playerGroup.getPlayerGroups());

        permissionHandler.saveConfig();
    }

    private void playerExists(final UUID uuid, final Consumer<Boolean> consumer){
        for (String s : permissionHandler.getConfiguration().getStringList("players")) {
            if(s.equals(uuid.toString())){
                consumer.accept(true);
                return;
            }
        }

        consumer.accept(false);
    }

    public List<PlayerGroup> getPlayers() {
        return players;
    }

    public List<String> getPlayersUUID() {
        return playersUUID;
    }
}
