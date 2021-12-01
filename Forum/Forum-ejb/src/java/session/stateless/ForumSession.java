package session.stateless;

import entity.Forum;
import entity.ForumThread;
import entity.Post;
import java.util.ArrayList;
import util.exception.NoResultException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.ForumExistsException;
import util.exception.ForumNotFoundException;
import util.exception.ThreadNotFoundException;


@Stateless
public class ForumSession implements ForumSessionLocal {
    
    @PersistenceContext
    private EntityManager em;
    
    @EJB
    private PostSessionLocal postSessionLocal;
    
    @EJB
    private ThreadSessionLocal threadSessionLocal;

    @Override
    public Forum retrieveForumById(Long fId) throws NoResultException {
        Forum forum = em.find(Forum.class, fId);

        if (forum != null) {
            return forum;
        } else {
            throw new NoResultException("Not found");
        }
    }
    
    @Override
    public List<Forum> searchForums(String forumName) {
        Query q;
        if (forumName != null) {
            q = em.createQuery("SELECT f FROM Forum f WHERE "
                    + "LOWER(f.name) LIKE :forumName");
            q.setParameter("name", "%" + forumName.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT f FROM Forum f");
        }

        return q.getResultList();
    }
    
    @Override
    public List<Forum> searchForumsByDescription(String forumDescription) {
        Query q;
        if (forumDescription != null) {
            q = em.createQuery("SELECT f FROM Forum f WHERE "
                    + "LOWER(f.description) LIKE :forumDescription");
            q.setParameter("name", "%" + forumDescription.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT f FROM Forum f");
        }

        return q.getResultList();
    }
    
    @Override
    public Forum retrieveForumByName(String forumName) throws ForumNotFoundException {
        Query query = em.createQuery("SELECT f FROM Forum f WHERE f.forumName = :inForumName");
        query.setParameter("inForumName", forumName);

        try {
            return (Forum) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new ForumNotFoundException("Forum " + forumName + " does not exist!");
        }
    }
    
    @Override
    public List<Forum> retrieveAllForums() {
        Query q = em.createQuery("SELECT f from Forum f");
        return q.getResultList();
    }

    @Override
    public void createForum(Forum f) throws ForumExistsException {
        List<String> forumNames = new ArrayList<>();
        for (Forum fr : retrieveAllForums()) {
            forumNames.add(fr.getForumName());
        }
        if (forumNames.contains(f.getForumName())) {
            throw new ForumExistsException("Forum already exists!");
        } else {
            em.persist(f);
            em.flush();
        }
    }
    
    @Override
    public void updateForum(Forum f) throws NoResultException, ForumExistsException {
        List<String> forumNames = new ArrayList<>();
        for (Forum fr : retrieveAllForums()) {
            forumNames.add(fr.getForumName());
        }
        if (forumNames.contains(f.getForumName())) {
            throw new ForumExistsException("Forum already exists!");
        } else {
            Forum oldForum = retrieveForumById(f.getId());    
            oldForum.setForumName(f.getForumName());
            oldForum.setForumDescription(f.getForumDescription());
        }
    }
    
    @Override
    public void deleteForum(Long fId) throws NoResultException, ThreadNotFoundException {
        Forum forum = retrieveForumById(fId);
        Query q = em.createQuery("SELECT t from ForumThread t WHERE t.forum.id = :inForumId");
        q.setParameter("inForumId", fId);
        List<ForumThread> threads = q.getResultList();
        
        if (!threads.isEmpty()) {
            List<Post> allPosts = postSessionLocal.retrieveAllPosts();
            for (ForumThread tr : threads) {
                for (Post p : allPosts) {
                    if (p.getThread().getId() == tr.getId()) {
                        postSessionLocal.deletePost(p.getId());
                    }
                }
                threadSessionLocal.deleteThread(tr.getId());
            }
        }
        em.remove(forum);
    }
    
//    @Override
//    public void deleteForum(Long forumId) throws NoResultException {
//        Forum forumToRemove = retrieveForumById(forumId);
//        em.remove(forumToRemove);
//    }
}
