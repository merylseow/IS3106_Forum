package session.stateless;

import entity.Forum;
import entity.ForumThread;
import entity.Post;
import util.exception.NoResultException;
import java.util.List;
import javax.ejb.Local;
import util.exception.ForumExistsException;
import util.exception.ForumNotFoundException;
import util.exception.ThreadNotFoundException;


@Local
public interface ForumSessionLocal {

    public void createForum(Forum f) throws ForumExistsException;

    public void updateForum(Forum f) throws NoResultException, ForumExistsException;

    public void deleteForum(Long fId) throws NoResultException, ThreadNotFoundException;

    public List<Forum> retrieveAllForums();

    public Forum retrieveForumById(Long fId) throws NoResultException;

    public Forum retrieveForumByName(String forumName) throws ForumNotFoundException;

    public List<Forum> searchForums(String forumName);

    public List<Forum> searchForumsByDescription(String forumDescription);
    
}
