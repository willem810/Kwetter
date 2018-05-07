package service;

import dao.GroupDao;
import dao.JPA.JPA;
import domain.Group;
import exception.GroupNotFoundException;
import interceptor.Logging;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.ArrayList;

@Stateless
@Logging
public class GroupService {

    @Inject
    @JPA
    GroupDao groupDao;


    public void setGroupDao(GroupDao dao) {
        this.groupDao = dao;
    }

    /**
     * addGroup adds a Group to the persistence unit
     * @param g the group to add
     */
    public void addGroup(Group g) {
        groupDao.addGroup(g);
    }

    /**
     * removeGroup removes a Group of the persistence unit
     * @param g the group to remove
     */
    public void removeGroup(Group g) {
        groupDao.removeGroup(g);
    }

    /**
     * getGroups returns all Groups
     * @return returns all Groups
     */
    public ArrayList<Group> getGroups() {
        return groupDao.getGroups();
    }

    /**
     * findByName returns a Group with the given name
     * @param name the name of the Group
     * @return a Group
     * @throws GroupNotFoundException is thrown when no group is found with the given name
     */
    public Group findByName(String name) throws GroupNotFoundException {
        return groupDao.findByName(name);
    }
}
