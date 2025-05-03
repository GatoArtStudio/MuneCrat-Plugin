package com.github.gatoartstudios.munecraft.core.enums;

public enum PlaceHoldersType {
    USERNAME("{USERNAME}"),
    PLAYERNAME("{PLAYERNAME}"),
    WORLDNAME("{WORLDNAME}");

    private final String value;

    PlaceHoldersType(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }
}
