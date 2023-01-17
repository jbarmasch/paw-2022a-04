package ar.edu.itba.paw.model;

public enum RoleEnum {
    ROLE_USER,
    ROLE_CREATOR,
    ROLE_BOUNCER;

    public static RoleEnum getRole(int id) {
        RoleEnum[] roles = RoleEnum.values();
        if (id >= roles.length || id < 0)
            return null;
        return roles[id];
    }

    public int getValue() {
        return ordinal() + 1;
    }
}
