package dao;

import domain.Group;
import exception.GroupNotFoundException;

import java.util.ArrayList;

public interface GroupDao {
    void addGroup(Group group);
    void removeGroup(Group group);
    ArrayList<Group> getGroups();
    Group findByName(String name) throws GroupNotFoundException;
}
