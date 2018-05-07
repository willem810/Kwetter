package dao.Collection;

import dao.GroupDao;
import domain.Group;

import java.util.ArrayList;

public class GroupDaoColl implements GroupDao {
    ArrayList<Group> groups = new ArrayList<>();


    @Override
    public void addGroup(Group group) {
        groups.add(group);
    }

    @Override
    public void removeGroup(Group group) {
        groups.remove(group);
    }

    @Override
    public ArrayList<Group> getGroups() {
        return groups;
    }

    @Override
    public Group findByName(String name) {
        for (Group g: groups) {
            if (g.getName().equals(name)) {
                return g;
            }
        }
        return null;
    }
}
