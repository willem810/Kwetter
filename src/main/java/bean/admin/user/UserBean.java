package bean.admin.user;

import bean.RedirectBean;
import domain.Group;
import domain.User;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named("userBean")
@RequestScoped
public class UserBean implements Serializable {
    @Inject
    private UserService uService;

    private String filter;

    public List<User> getUsers() {
        List<User> users = uService.getUsers();
        if (filter != null && filter.length() > 0) {
            List<User> filtered = new ArrayList<>();
            for (User u : users) {
                if (u.getUsername().toLowerCase().contains(filter)) {
                    filtered.add(u);
                } else if (u.getName().toLowerCase().contains(filter)) {
                    filtered.add(u);
                }

                for (Group g : u.getGroups()) {
                    if (g.getName().toLowerCase().contains(filter)) {
                        filtered.add(u);
                    }
                }
            }

            return filtered;
        }

        return users;
    }

    public void removeUser(User user) {
        uService.removeUser(user);
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String goToUserDetail() {
        return RedirectBean.redirect("/admin/users/userDetail.xhtml");
    }

    public String goToAddUser() {
        return RedirectBean.redirect("/admin/users/userAdd.xhtml");
    }
}
