package session.stateless;

import entity.Forum;
import entity.ForumThread;
import entity.ForumUser;
import util.exception.ThreadNotFoundException;
import java.util.List;
import javax.ejb.Local;
import util.exception.ForumNotFoundException;
import util.exception.NoResultException;
import util.exception.ThreadExistsException;
import util.exception.UserNotFoundException;


@Local
public interface ThreadSessionLocal {

    public List<ForumThread> retrieveAllThreads();

    public ForumThread retrieveThreadById(Long threadId) throws ThreadNotFoundException;

    public ForumThread retrieveThreadByThreadName(String threadName) throws ThreadNotFoundException;

    public void deleteThread(Long threadId, Long forumId) throws ThreadNotFoundException, NoResultException;

    public void updateThread(ForumThread thread) throws ThreadNotFoundException, ThreadExistsException;

    public List<ForumThread> retrieveThreadsByForumId(Long forumId);

    public ForumUser retrieveUserByThreadId(Long threadId);

    public void openThread(Long threadId) throws ThreadNotFoundException, ThreadExistsException;

    public void closeThread(Long threadId) throws ThreadNotFoundException, ThreadExistsException;

    public void createThread(ForumThread newThread, Long forumId, Long userId) throws NoResultException, ThreadExistsException, UserNotFoundException, ThreadNotFoundException;

    public int retrieveThreadCount(Long forumId) throws ThreadNotFoundException;

    public Forum retrieveForumById(Long fId) throws NoResultException;

    public void deleteThread(Long threadId) throws ThreadNotFoundException, NoResultException;
    
}
