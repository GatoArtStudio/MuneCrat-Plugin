package com.github.gatoartstudios.munecraft.models;

public class VipModel {
    private long idVip;
    private String name;
    private String kit;
    private String groupLuckperms;

    public VipModel(long idVip, String name, String kit, String groupLuckperms) {
        this.idVip = idVip;
        this.name = name;
        this.kit = kit;
        this.groupLuckperms = groupLuckperms;
    }

    public VipModel() {
    }

    public long getIdVip() {
        return idVip;
    }

    public void setIdVip(long idVip) {
        this.idVip = idVip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKit() {
        return kit;
    }

    public void setKit(String kit) {
        this.kit = kit;
    }

    public String getGroupLuckperms() {
        return groupLuckperms;
    }

    public void setGroupLuckperms(String groupLuckperms) {
        this.groupLuckperms = groupLuckperms;
    }
}
