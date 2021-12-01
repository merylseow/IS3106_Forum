package session.singleton;

import entity.Forum;
import entity.ForumThread;
import entity.ForumUser;
import entity.Post;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import session.stateless.ForumSessionLocal;
import session.stateless.PostSessionLocal;
import session.stateless.ThreadSessionLocal;
import session.stateless.ForumUserSessionLocal;
import util.exception.ForumExistsException;
import util.exception.NoResultException;
import util.exception.PostExistsException;
import util.exception.PostNotFoundException;
import util.exception.ThreadExistsException;
import util.exception.ThreadNotFoundException;
import util.exception.UserExistsException;
import util.exception.UserNotFoundException;

@Singleton
@LocalBean
@Startup
public class DataInitializationSession {

    @EJB
    private ForumUserSessionLocal forumUserSessionLocal;
    @EJB
    private PostSessionLocal postSessionLocal;
    @EJB
    private ThreadSessionLocal threadSessionLocal;
    @EJB
    private ForumSessionLocal forumSessionLocal;

    @PersistenceContext
    private EntityManager em;

    public DataInitializationSession() {
    }

    @PostConstruct
    public void postConstruct() {
        List<ForumUser> users = forumUserSessionLocal.retrieveAllUsers();
        if (users.isEmpty()) {
            initialiseUsers();
            initialiseForums();
            initialiseThreads();
            initialisePosts();
        }
    }
    
    private void initialiseUsers() {
        try {
            forumUserSessionLocal.createUser(new ForumUser("lekhsianghui", "password", "Lek", "Hsiang Hui", false, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("05/05/1980 08:00:00"), new Date()));
            forumUserSessionLocal.createUser(new ForumUser("admin1", "password", "Admin", "One", true, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("23/09/1999 08:00:00"), new Date()));
            forumUserSessionLocal.createUser(new ForumUser("merylseow", "password", "Meryl", "Seow", false, new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("12/09/2000 08:00:00"), new Date()));
        } catch (ParseException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserExistsException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialiseForums() {
        try {
            forumSessionLocal.createForum(new Forum("Squid Game", "Hello everyone this is a forum to discuss anything about Squid Game. No spoilers please!"));
            forumSessionLocal.createForum(new Forum("IS3106", "This forum is to discuss queries regarding IS3106. Let's help one another!"));
            forumSessionLocal.createForum(new Forum("Internships", "If you have any experiences on tech interviews, feel free to share here!"));
        } catch (ForumExistsException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialiseThreads() {
        try {
            threadSessionLocal.createThread(new ForumThread("Episode 1", "Everything about ep 1. Please be considerate, no spoilers please!"), new Long(1), new Long(3));
            threadSessionLocal.createThread(new ForumThread("Episode 2", "Everything about ep 2. Please be considerate, no spoilers please!"), new Long(1), new Long(3));
            threadSessionLocal.createThread(new ForumThread("Episode 3", "Everything about ep 3. Please be considerate, no spoilers please!"), new Long(1), new Long(3));
        } catch (UserNotFoundException | ThreadNotFoundException | NoResultException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ThreadExistsException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initialisePosts() {
        try {
            postSessionLocal.createPost(new Post("So chilling!", "I am living for this show. It rocks."), new Long(1), new Long(3));
            postSessionLocal.createPost(new Post("What happens next?", "Can't wait for the next episode!"), new Long(1), new Long(1));
            postSessionLocal.createPost(new Post("Red Light, Green Light", "Just a random thought. Would you participate in this game?"), new Long(1), new Long(3));
        } catch (PostNotFoundException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ThreadNotFoundException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UserNotFoundException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        } catch (PostExistsException ex) {
            Logger.getLogger(DataInitializationSession.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
