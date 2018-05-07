package domain;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.persistence.*;

@Entity
@NamedQueries({
        @NamedQuery(name = "hashtag.findByName", query = "SELECT h FROM Hashtag h WHERE h.name = :name")
})
public class Hashtag {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String name;


    public Hashtag() {
    }

    public Hashtag(String name) {
        this.name = name;
    }

    public Long getId() { return this.id;}
    public void setId(Long id ) { this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject toJson() {
        JsonObjectBuilder objBuilder = Json.createObjectBuilder().
                add("id", id).
                add("name", name);

        return objBuilder.build();
    }
}
