package ar.edu.itba.paw.model;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_roleid_seq")
    @SequenceGenerator(sequenceName = "roles_roleid_seq", name = "roles_roleid_seq", allocationSize = 1)
    @Column(name = "roleid")
    private int roleId;
    @Column(length = 100, nullable = false, name = "name")
    private String roleName;

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public Role() {}

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }
}

