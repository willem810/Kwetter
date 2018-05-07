package dao.JPA;

import dao.GroupDao;
import domain.Group;
import exception.GroupNotFoundException;
import interceptor.Managed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

@Stateless
@JPA
public class GroupDaoJPA implements GroupDao {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    public GroupDaoJPA() {

    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addGroup(Group group) {
        em.persist(group);
    }

    @Override
    @Managed
    public void removeGroup(Group group) {
        em.remove(group);
    }

    @Override
    public ArrayList<Group> getGroups() {
        Query query = em.createQuery("SELECT g FROM GROUPS g");
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public Group findByName(String name) throws GroupNotFoundException {
        TypedQuery<Group> query = em.createNamedQuery("group.findByName", Group.class);
        query.setParameter("name", name);
        List<Group> result = query.getResultList();
        if (result.size() <= 0) {
            throw new GroupNotFoundException(name);
        }

        return result.get(0);
    }
}
