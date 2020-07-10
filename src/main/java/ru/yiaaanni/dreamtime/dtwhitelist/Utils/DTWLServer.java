package ru.yiaaanni.dreamtime.dtwhitelist.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ConfigurationAdapter;
import net.md_5.bungee.api.config.ServerInfo;
import ru.yiaaanni.dreamtime.dtwhitelist.DTWhitelist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DTWLServer {
    private String id;
    private boolean enabled;
    private boolean perm;
    private Set<String> list;
    private boolean kick;
    private String reason;
    private boolean test;

    public DTWLServer(String id, boolean enabled, boolean perm, Set<String> list, boolean kick, String reason, boolean test) {
        this.id = id;
        this.enabled = enabled;
        this.perm = perm;
        this.list = list;
        this.kick = kick;
        this.reason = reason;
        this.test = test;
    }

    public String getId() {
        return id;
    }

    public Set<String> getList() {
        return list;
    }

    public boolean isTest() {
        return test;
    }

    public String getReason() {
        return reason;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isKick() {
        return kick;
    }

    public boolean isPerm() {
        return perm;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setList(Set<String> list) {
        this.list = list;
    }

    public void setPerm(boolean perm) {
        this.perm = perm;
    }

    public void setKick(boolean kick) {
        this.kick = kick;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public static DTWLServer getServerForName(String serverId) {

        for(DTWLServer se : DTWhitelist.servers) {
            if(se.getId().equalsIgnoreCase(serverId)) {
                return se;
            }
        }
        return createDefaultServer(serverId);
    }


    private static DTWLServer createDefaultServer(String serverId) {
        ConfigurationAdapter adapter = ProxyServer.getInstance().getConfigurationAdapter();
        Map<String, ServerInfo> bungeeServers = adapter.getServers();
        ServerInfo target = bungeeServers.get(serverId);
        if (target != null) {

            DTWLServer defServer = new DTWLServer(serverId, false, false, new HashSet<>(), false, ChatColor.translateAlternateColorCodes('&', "&cВы не можете подключиться к серверу!"), false);
            saveServer(defServer);
            DTWhitelist.servers.add(defServer);
            return defServer;
        }
        return null;
    }

    private static void saveServer(DTWLServer server) {
        server.save();
    }

    public void save(){
        save(true);
    }
    public void save(boolean saveConfig) {

        DTWhitelist.cfg.set("servers." + this.id + ".enable", this.enabled);
        DTWhitelist.cfg.set("servers." + this.id + ".withperm", this.perm);
        DTWhitelist.cfg.set("servers." + this.id + ".list", new ArrayList<>(this.list));
        DTWhitelist.cfg.set("servers." + this.id + ".kick", this.kick);
        DTWhitelist.cfg.set("servers." + this.id + ".kickreason", this.reason);
        DTWhitelist.cfg.set("servers." + this.id + ".intest", this.test);
        if (saveConfig) {
            DTWhitelist.ins.saveCfg();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTWLServer that = (DTWLServer) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return this.getId();
    }
}
