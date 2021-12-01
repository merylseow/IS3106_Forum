package session.stateless;

import entity.Forum;
import entity.ForumThread;
import entity.ForumUser;
import java.util.ArrayList;
import util.exception.ThreadNotFoundException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.NoResultException;
import util.exception.ThreadExistsException;
import util.exception.UserNotFoundException;

@Stateless
public class ThreadSession implements ThreadSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ForumUserSessionLocal forumUserSessionLocal;

    public ThreadSession() {
    }

    @Override
    public void createThread(ForumThread newThread, Long forumId, Long userId) throws NoResultException, ThreadExistsException, UserNotFoundException, ThreadNotFoundException {
        if (newThread != null) {
            List<String> threadNames = new ArrayList<>();
            for (ForumThread tr : retrieveAllThreads()) {
                threadNames.add(tr.getThreadName());
            }
            if (threadNames.contains(newThread.getThreadName())) {
                throw new ThreadExistsException("Thread already exists!");
            } else {
                Forum f = retrieveForumById(forumId);
                ForumUser u = forumUserSessionLocal.retrieveUserByUserId(userId);
                f.setThreadCount(retrieveThreadCount(forumId) + 1);
                newThread.setForum(f);
                newThread.setForumUser(u);
                em.persist(newThread);
                em.flush();
            }
        }
    }

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
    public List<ForumThread> retrieveAllThreads() {
        Query query = em.createQuery("SELECT t FROM ForumThread t");
        return query.getResultList();
    }

    @Override
    public ForumThread retrieveThreadById(Long threadId) throws ThreadNotFoundException {
        ForumThread thread = em.find(ForumThread.class, threadId);

        if (thread != null) {
            return thread;
        } else {
            throw new ThreadNotFoundException("Thread ID" + threadId + " does not exist!");
        }
    }

    @Override
    public ForumThread retrieveThreadByThreadName(String threadName) throws ThreadNotFoundException {
        Query query = em.createQuery("SELECT t FROM ForumThread t WHERE t.threadName = :inThreadName");
        query.setParameter("inThreadName", threadName);

        try {
            return (ForumThread) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new ThreadNotFoundException("Thread " + threadName + " does not exist!");
        }
    }

    @Override
    public List<ForumThread> retrieveThreadsByForumId(Long forumId) {
        Query query = em.createQuery("SELECT t FROM ForumThread t WHERE t.forum.id = :inForumId");
        query.setParameter("inForumId", forumId);
        return query.getResultList();
    }

    @Override
    public void updateThread(ForumThread t) throws ThreadNotFoundException, ThreadExistsException {
        List<String> threadNames = new ArrayList<>();
        for (ForumThread tr : retrieveAllThreads()) {
            threadNames.add(tr.getThreadName());
        }
        if (threadNames.contains(t.getThreadName())) {
            throw new ThreadExistsException("Thread already exists!");
        } else {
            ForumThread oldThread = retrieveThreadById(t.getId());
            oldThread.setThreadName(t.getThreadName());
            oldThread.setThreadDescription(t.getThreadDescription());
        }
    }

    @Override
    public void deleteThread(Long threadId) throws ThreadNotFoundException, NoResultException {
        ForumThread threadToRemove = retrieveThreadById(threadId);
        em.remove(threadToRemove);
    }

    @Override
    public void deleteThread(Long threadId, Long forumId) throws ThreadNotFoundException, NoResultException {
        ForumThread threadToRemove = retrieveThreadById(threadId);
        Forum f = retrieveForumById(forumId);
        f.setThreadCount(retrieveThreadCount(forumId) - 1);
        em.remove(threadToRemove);
    }

    @Override
    public ForumUser retrieveUserByThreadId(Long threadId) {
        Query query = em.createQuery("SELECT u FROM ForumUser u, IN (u.threads) t WHERE t.id = :inThreadId");
        query.setParameter("inThreadId", threadId);
        return (ForumUser) query.getSingleResult();
    }

    @Override
    public void openThread(Long threadId) throws ThreadNotFoundException, ThreadExistsException {
        ForumThread t = retrieveThreadById(threadId);
        t.setStatus("OPENED");
        updateThread(t);
    }

    @Override
    public void closeThread(Long threadId) throws ThreadNotFoundException, ThreadExistsException {
        ForumThread t = retrieveThreadById(threadId);
        t.setStatus("CLOSED");
        updateThread(t);
    }

    @Override
    public int retrieveThreadCount(Long forumId) throws ThreadNotFoundException {
        Query query = em.createQuery("SELECT t FROM ForumThread t WHERE t.forum.id = :inForumId");
        query.setParameter("inForumId", forumId);
        return query.getResultList().size();
    }
}
