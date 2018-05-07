package bean.admin.user;

import bean.RedirectBean;
import domain.Group;
import domain.User;
import exception.GroupNotFoundException;
import exception.UserNotFoundException;
import service.GroupService;
import service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userDetailBean")
@ViewScoped
public class UserDetailBean implements Serializable {
    @Inject
    UserService uService;
    @Inject
    GroupService gService;
    private String username;

    private User user;

    private Group group;
    private String selectedGroup = "--Select Group--";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User u) {
        this.user = u;
    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(String groupName) {
        this.selectedGroup = groupName;
    }

    public void loadUser() {
        try {
            User u = uService.findByUsername(username);
            setUser(u);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }

    public List<Group> getRemainingGroups() {
        List<Group> groups = new ArrayList<>();
        if (user != null) {
            List<Group> userGroups = user.getGroups();

            for (Group group : gService.getGroups()) {
                boolean exists = false;
                for (Group userGroup : userGroups) {
                    if (userGroup.getId() == group.getId()) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) groups.add(group);
            }
        }

        return groups;
    }

    @RolesAllowed({"Admin"})
    public void addGroup() {
        String groupName = getSelectedGroup();

        if (groupName == null || groupName.equals("")) return;

        try {
            this.group = gService.findByName(groupName);
        } catch (GroupNotFoundException e) {
            e.printStackTrace();
        }

        if (group != null) {
            uService.addGroup(user, group);
        }
    }

    @RolesAllowed({"Admin"})
    public void removeGroup(Group group) {
//        try {
//            this.group = gService.findByName(groupName);
//        }catch(GroupNotFoundException e) {
//            e.printStackTrace();
//        }

        if (group != null) {
            uService.removeGroup(user, group);
        }
    }

    public String goToUsers() {
        return RedirectBean.redirect("/admin/users/index.xhtml");
    }
}
