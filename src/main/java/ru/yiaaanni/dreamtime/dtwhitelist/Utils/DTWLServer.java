package ru.yiaaanni.dreamtime.dtwhitelist.Utils;

import java.util.List;

public class DTWLServer {
    private String id;
    private boolean enabled;
    private boolean perm;
    private List<String> list;
    private boolean kick;
    private String reason;
    private boolean test;

    public DTWLServer(String id, boolean enabled, boolean perm, List<String> list, boolean kick, String reason, boolean test) {
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

    public List<String> getList() {
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

    public void setList(List<String> list) {
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
}
