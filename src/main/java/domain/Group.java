package domain;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity( name = "GROUPS")
@NamedQueries({
        @NamedQuery(name = "group.findByName", query = "SELECT g FROM GROUPS g WHERE g.name = :name")
})
public class Group {
    public static final String ADMIN_GROUP="AdminGroup", USER_GROUP="UserGroup";

    @Id
    @GeneratedValue
    private Long id;

    @ElementCollection(targetClass = Permissions.class)
    @Enumerated(EnumType.STRING)
    private List<Permissions> permissions = new ArrayList<>();

    @ManyToMany @JoinTable(
            name="GROUP_INHERIT",
            joinColumns=@JoinColumn(name="GROUP_ID", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="INHERIT_ID", referencedColumnName="id"))
    private List<Group> InheritedGroups = new ArrayList<>();

    @Column(unique = true)
    private String name;

    public Group() {
    }

    public Group(String name, List<Permissions> permissions) {
        this.name= name;
        this.permissions = permissions;
    }

    public Group(String name, List<Permissions> permissions, List<Group> inhertitedGroups) {
        this.name= name;
        this.permissions = permissions;
        this.InheritedGroups = inhertitedGroups;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName(){ return name; }
    public void setName(String name) { this.name = name; }

    public List<Permissions> getPermissions() { return permissions;}
    public void setPermissions(List<Permissions> permissions) { this.permissions = permissions;}
    public void addPermission(Permissions p) { permissions.add(p);}
    public void removePermission(Permissions p) {permissions.remove(p);}
    public boolean hasPermission(Permissions p) {
        boolean contains =  permissions.contains(p);
        if(contains) {
            return true;
        }



        for(Group inherit : this.InheritedGroups) {
            if(inherit.hasPermission(p)){
                return true;
            }
        }

        return false;
    }

    public JsonObject toJson() {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder().
                add("id", id).
                add("name", name);

        JsonArrayBuilder permissionBuilder = Json.createArrayBuilder();
        if (permissions.size() > 0) {
            for (Permissions p : permissions) {
                permissionBuilder.add(p.toString());
            }
        }
        objBuilder.add("permissions", permissionBuilder);

        JsonArrayBuilder intheritedBuilder = Json.createArrayBuilder();
        if (InheritedGroups.size() > 0) {
            for (Group g : InheritedGroups) {
                intheritedBuilder.add(g.getName());
            }
        }
        objBuilder.add("groups", intheritedBuilder);

        return objBuilder.build();
    }
}
