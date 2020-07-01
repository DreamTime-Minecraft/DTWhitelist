package ru.yiaaanni.dreamtime.dtwhitelist;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
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
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

public final class DTWhitelist extends Plugin implements Listener {

    public static Configuration cfg;
    public static DTWhitelist ins;
    public static Set<DTWLServer> servers = new HashSet<>(); // HashSet, чтобы наверняка не было одинаковых серверов в списке

    @Override
    public void onEnable() {
        ins = this;
        getProxy().getPluginManager().registerCommand(this, new DTWLCommand("dtwl"));
        config();
        addAllServers();
        saveCfg();
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
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

        try {
            cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(config);
        }catch (Exception e) {}
    }

    private void addAllServers() {
        Configuration servs = cfg.getSection("servers");
        for(String s : servs.getKeys()) {
            boolean enable = cfg.getBoolean("servers."+s+".enable");
            boolean perm = cfg.getBoolean("servers."+s+".withperm");
            Set<String> list = new HashSet<>(cfg.getStringList("servers." + s + ".list"));
            boolean kick = cfg.getBoolean("servers."+s+".kick");
            String reason = cfg.getString("servers."+s+".kickreason");
            boolean test = cfg.getBoolean("servers."+s+".intest");

            DTWLServer server = new DTWLServer(s, enable, perm, list, kick, reason, test);
            server.save(false);
            servers.add(server);
        }
    }

    public void saveCfg() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(cfg, new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final ReentrantLock lock = new ReentrantLock();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PostLoginEvent e) {
        ProxiedPlayer p = e.getPlayer();

            if (p.hasPermission("dtwl.join.*")) {
                return;
            }

            DTWLServer server = DTWLServer.getServerForName("_bungee_");
            onServerConnect(server, p);
    }

    @EventHandler
    public void onChangeServer(ServerSwitchEvent e) {
        ProxiedPlayer p = e.getPlayer();

            if (p.hasPermission("dtwl.join.*")) {
                return;
            }

            DTWLServer server = DTWLServer.getServerForName(p.getServer().getInfo().getName());
            onServerConnect(server, p);
    }

    private void onServerConnect(DTWLServer server, ProxiedPlayer p) {
        if(server == null) {
            return;
        }

        if (server.isEnabled()) {
            if (server.isPerm()) {
                if (!p.hasPermission("dtwl.join." + server.getId())) {
                    String reason = server.getReason();
                    String kickmsg = cfg.getString("messages.kickmsg")
                            .replace("\\n", "\n").replace("{reason}", reason);

                    p.disconnect(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', kickmsg)));
                }
            } else {
                if (!server.getList().contains(p.getName().toLowerCase())) {
                    String reason = server.getReason();
                    String kickmsg = cfg.getString("messages.kickmsg")
                            .replace("\\n", "\n").replace("{reason}", reason);

                    p.disconnect(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', kickmsg)));
                }
            }
        }

        if (server.isTest()) {
            p.sendMessage(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',
                    cfg.getString("messages.intest")
                            .replace("{prefix}", cfg.getString("messages.prefix"))
            )));
        }
    }

}
