package bean.admin.user;


import bean.RedirectBean;
import domain.Group;
import domain.User;
import exception.GroupNotFoundException;
import service.GroupService;
import service.UserService;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userAddBean")
@ViewScoped
public class UserAddBean implements Serializable {

    private String username;
    private String password;
    private String confPassword;
    private String name;
    private String location;
    private String web;
    private String bio;
    private String selectedGroup;

    private List<Group> groups = new ArrayList<>();

    @Inject
    private UserService uService;

    @Inject
    GroupService gService;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfPassword() {
        return confPassword;
    }

    public void setConfPassword(String confPassword) {
        this.confPassword = confPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void addUser() {
        if (username != null && password != null && name != null) {
            User u = new User(username, password, name, web, bio, groups);
            uService.addUser(u);
        }
    }

    public List<Group> getAvailableGroups() {
        return gService.getGroups();
    }

    public List<Group> getRemainingGroups() {
        List<Group> remainingGroups = new ArrayList<>();

        for (Group group : getAvailableGroups()) {
            boolean exists = false;
            for (Group selectedGroup : groups) {
                if (selectedGroup.getId() == group.getId()) {
                    exists = true;
                    break;
                }
            }
            if (!exists) remainingGroups.add(group);
        }


        return remainingGroups;
    }

    public void addGroup() {
       // String groupName = getSelectedGroup();
         String groupName = selectedGroup;

//
        if (groupName == null || groupName.equals("")) return;

        try {
            Group g = gService.findByName(groupName);
            boolean exists = false;
            for(Group group : groups) {
                if(group.getName().equals(g.getName())){
                    exists = true;
                    break;
                }
            }

            if(!exists){
                groups.add(g);
            }
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }

//        if (selectedGroup != null) {
//            groups.add(selectedGroup);
//        }
    }

    public void removeGroup(Group group) {
        if(group != null) {
            groups.remove(group);
        }
    }

    public String goToUsers() {
        return RedirectBean.redirect("/admin/users/index.xhtml");
    }
}
