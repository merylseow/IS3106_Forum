package session.stateless;

import entity.ForumThread;
import entity.ForumUser;
import entity.Post;
import java.util.ArrayList;
import util.exception.NoResultException;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import util.exception.PostNotFoundException;
import javax.persistence.Query;
import util.exception.PostExistsException;
import util.exception.ThreadNotFoundException;
import util.exception.UserNotFoundException;

@Stateless
public class PostSession implements PostSessionLocal {

    @PersistenceContext
    private EntityManager em;

    @EJB
    private ForumUserSessionLocal forumUserSessionLocal;

    public PostSession() {
    }

    @Override
    public List<Post> searchPosts(String postName) {
        Query q;
        if (postName != null) {
            q = em.createQuery("SELECT p FROM Post p WHERE "
                    + "LOWER(p.name) LIKE :postName");
            q.setParameter("name", "%" + postName.toLowerCase() + "%");
        } else {
            q = em.createQuery("SELECT p FROM Post p");
        }

        return q.getResultList();
    }

    @Override
    public Long createPost(Post newPost, Long threadId, Long userId) throws PostNotFoundException, PostExistsException, ThreadNotFoundException, UserNotFoundException {
        if (newPost != null) {
            List<String> postNames = new ArrayList<>();
            for (Post po : retrieveAllPosts()) {
                postNames.add(po.getTitle());
            }
            if (postNames.contains(newPost.getTitle())) {
                throw new PostExistsException("Post already exists!");
            } else {
                ForumThread t = retrieveThreadById(threadId);
                ForumUser u = forumUserSessionLocal.retrieveUserByUserId(userId);
                t.setPostCount(retrievePostCount(threadId) + 1);
                newPost.setThread(t);
                newPost.setUser(u);
                em.persist(newPost);
                em.flush();
            }
        }
        return newPost.getId();
    }

    @Override
    public Post getPost(Long pId) throws NoResultException {
        Post post = em.find(Post.class, pId);

        if (post != null) {
            return post;
        } else {
            throw new NoResultException("Not found");
        }
    }

    @Override
    public void updatePost(Post p) throws PostNotFoundException, PostExistsException {
        List<String> postNames = new ArrayList<>();
        for (Post po : retrieveAllPosts()) {
            postNames.add(po.getTitle());
        }
        if (postNames.contains(p.getTitle())) {
            throw new PostExistsException("Post already exists!");
        } else {
            Post oldPost = retrievePostById(p.getId());
            oldPost.setTitle(p.getTitle());
            oldPost.setText(p.getText());
        }
    }

    @Override
    public void deletePost(Long postId) throws NoResultException {
        Post postToRemove = getPost(postId);
        em.remove(postToRemove);
    }

    @Override
    public void deletePost(Long postId, Long threadId) throws ThreadNotFoundException, NoResultException {
        Post postToRemove = getPost(postId);
        ForumThread t = retrieveThreadById(threadId);
        t.setPostCount(retrievePostCount(threadId) - 1);
        em.remove(postToRemove);
    }

    @Override
    public Post retrievePostByName(String postName) throws PostNotFoundException {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.title = :inTitle");
        query.setParameter("inTitle", postName);

        try {
            return (Post) query.getSingleResult();
        } catch (NonUniqueResultException ex) {
            throw new PostNotFoundException("Post " + postName + " does not exist!");
        }
    }

    @Override
    public Post retrievePostById(Long postId) throws PostNotFoundException {
        Post post = em.find(Post.class, postId);

        if (post != null) {
            return post;
        } else {
            throw new PostNotFoundException("Post ID" + postId + " does not exist!");
        }
    }

    @Override
    public List<Post> retrieveAllPosts() {
        Query query = em.createQuery("SELECT p FROM Post p");
        return query.getResultList();
    }

    @Override
    public List<Post> retrievePostsByThreadId(Long threadId) {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.thread.id = :inThreadId");
        query.setParameter("inThreadId", threadId);
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
    public int retrievePostCount(Long threadId) throws ThreadNotFoundException {
        Query query = em.createQuery("SELECT p FROM Post p WHERE p.thread.id = :inThreadId");
        query.setParameter("inThreadId", threadId);
        return query.getResultList().size();
    }
}
