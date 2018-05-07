package bean.group;

import domain.Group;
import service.GroupService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named("groupBean")
@RequestScoped
public class GroupBean {

    @Inject
    GroupService gService;

    public List<Group> getGroups() {
        List<Group> groups = gService.getGroups();
//        if (filter != null && filter.length() > 0) {
//            List<User> filtered = new ArrayList<>();
//            for (User u : users) {
//                if (u.getUsername().toLowerCase().contains(filter)) {
//                    filtered.add(u);
//                } else if (u.getName().toLowerCase().contains(filter)) {
//                    filtered.add(u);
//                }
//
//                for (Group g : u.getGroups()) {
//                    if (g.getName().toLowerCase().contains(filter)) {
//                        filtered.add(u);
//                    }
//                }
//            }
//
//            return filtered;
//        }

        return groups;
    }
}
