package dao.JPA;

import dao.HashtagDao;
import domain.Hashtag;
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
public class HashtagDaoJPA implements HashtagDao {
    @PersistenceContext(unitName = "KwetterPU")
    private EntityManager em;

    public HashtagDaoJPA() {

    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Override
    public void addHashtag(Hashtag hashtag) {
        em.persist(hashtag);
    }

    @Override
    @Managed
    public void removeHashtag(Hashtag hashtag) {
        em.remove(hashtag);
    }

    @Override
    public ArrayList<Hashtag> getHashtags() {
        Query query = em.createQuery("SELECT h FROM Hashtag h");
        return new ArrayList<>(query.getResultList());
    }

    @Override
    public Hashtag findByName(String name) {
        TypedQuery<Hashtag> query = em.createNamedQuery("hashtag.findByName", Hashtag.class);
        query.setParameter("name", name);
        List<Hashtag> result = query.getResultList();
        if (result.size() <= 0) {
            Hashtag newTag = new Hashtag(name);
            addHashtag(newTag);
            return newTag;
        }

        return result.get(0);
    }
}









