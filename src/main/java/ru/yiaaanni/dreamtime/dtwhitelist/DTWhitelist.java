package ru.yiaaanni.dreamtime.dtwhitelist;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import ru.yiaaanni.dreamtime.dtwhitelist.Utils.DTWLServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class DTWhitelist extends Plugin implements Listener {

    public static Configuration cfg;

    public static List<DTWLServer> servers = new ArrayList<>();

    @Override
    public void onEnable() {
        command();
        config();
        addAllServers();

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) { }
    }

    private void command() {
        getProxy().getPluginManager().registerCommand(this, new DTWLCommand("dtwl","dtwl.admin","dtwhitelist"));
    }

    private void config() {
        File datapath = new File(getDataFolder().getPath());
        if(!datapath.exists()) {
            datapath.mkdir();
        }

        File config = new File(getDataFolder().getPath(), "config.yml");
        if(!config.exists()) {
            try (InputStream is = getResourceAsStream("config.yml")) {
                Files.copy(is, config.toPath());
            }catch (IOException e) {}
        }
    }

    private void addAllServers() {
        Collection<String> servs = cfg.getSection("servers").getKeys();
        for(String s : servs) {
            boolean enable = cfg.getBoolean("servers."+s+".enable");
            boolean perm = cfg.getBoolean("servers."+s+".withperm");
            List<String> list = cfg.getStringList("servers."+s+".list");
            boolean kick = cfg.getBoolean("servers."+s+".kick");
            String reason = cfg.getString("servers."+s+".kickreason");
            boolean test = cfg.getBoolean("servers."+s+".intest");

            servers.add(new DTWLServer(s,enable,perm,list,kick,reason,test));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(ServerConnectEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if(e.isCancelled() && e.getReason() != ServerConnectEvent.Reason.JOIN_PROXY) return;

        if(p.hasPermission("dtwl.join.*")) {
            return;
        }

        DTWLServer server = null;
        for(DTWLServer ser : servers) {
            if(ser.getId().equals("_bungee_")) {
                server = ser;
                continue;
            }
        }

        if(server == null) {
            return;
        }

        if(server.isEnabled()) {
            if(server.isTest()) {
                p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
            }
            return;
        }

        if(server.isPerm()) {
            if(!p.hasPermission("dtwl.join."+server.getId())) {
                String reason = server.getReason();
                String kickmsg = cfg.getString("messages.kickmsg")
                        .replace("\\n","\n").replace("{reason}",reason);

                p.disconnect(TextComponent.fromLegacyText(kickmsg));
                return;
            } else {
                if(server.isTest()) {
                    p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
                }
            }
        } else {
            if(!server.getList().contains(p.getName().toLowerCase())) {
                String reason = server.getReason();
                String kickmsg = cfg.getString("messages.kickmsg")
                        .replace("\\n","\n").replace("{reason}",reason);

                p.disconnect(TextComponent.fromLegacyText(kickmsg));
                return;
            } else {
                if(server.isTest()) {
                    p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
                }
            }
        }
    }

    @EventHandler
    public void onChangeServer(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();

        if(p.hasPermission("dtwl.join.*")) {
            return;
        }

        DTWLServer server = null;
        for(DTWLServer ser : servers) {
            if(ser.getId().equals(p.getServer().getInfo().getName())) {
                server = ser;
                continue;
            }
        }

        if(server == null) {
            return;
        }

        if(server.isEnabled()) {
            if(server.isTest()) {
                p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
            }
            return;
        }

        if(server.isPerm()) {
            if(!p.hasPermission("dtwl.join."+server.getId())) {
                String reason = server.getReason();
                String kickmsg = cfg.getString("messages.kickmsg")
                        .replace("\\n","\n").replace("{reason}",reason);

                p.disconnect(TextComponent.fromLegacyText(kickmsg));
                return;
            } else {
                if(server.isTest()) {
                    p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
                }
            }
        } else {
            if(!server.getList().contains(p.getName().toLowerCase())) {
                String reason = server.getReason();
                String kickmsg = cfg.getString("messages.kickmsg")
                        .replace("\\n","\n").replace("{reason}",reason);

                p.disconnect(TextComponent.fromLegacyText(kickmsg));
                return;
            } else {
                if(server.isTest()) {
                    p.sendMessage(TextComponent.fromLegacyText(cfg.getString("messages.intest")));
                }
            }
        }
    }
}
