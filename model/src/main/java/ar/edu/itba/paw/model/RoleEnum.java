package ar.edu.itba.paw.model;

public enum RoleEnum {
    USER,
    CREATOR,
    BOUNCER;

    public static RoleEnum getRole(int id) {
        RoleEnum[] roles = RoleEnum.values();
        if (id >= roles.length || id < 0)
            return null;
        return roles[id];
    }
}
