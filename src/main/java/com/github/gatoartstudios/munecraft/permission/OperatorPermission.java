package com.github.gatoartstudios.munecraft.permission;

import com.github.gatoartstudios.munecraft.core.interfaces.Permission;

public enum OperatorPermission implements Permission {
    STAFF("munecraft.staff"),
    STAFF_OP("munecraft.staff.op"),
    STAFFCHAT("munecraft.staffchat"),
    DEVELOPMENT("munecraft.development");

    private final String permission;

    OperatorPermission(String permission) {
        this.permission = permission;
    }


    @Override
    public String getPermission() {
        return permission;
    }
}
