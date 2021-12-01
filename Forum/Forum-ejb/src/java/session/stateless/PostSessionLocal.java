package session.stateless;

import entity.ForumThread;
import entity.Post;
import util.exception.NoResultException;
import java.util.List;
import javax.ejb.Local;
import util.exception.PostExistsException;
import util.exception.PostNotFoundException;
import util.exception.ThreadNotFoundException;
import util.exception.UserNotFoundException;


@Local
public interface PostSessionLocal {

    public List<Post> searchPosts(String postName);

    public Post getPost(Long pId) throws NoResultException;

    public void updatePost(Post p) throws PostNotFoundException, PostExistsException;

    public void deletePost(Long pId) throws NoResultException;

    public List<Post> retrieveAllPosts();

    public Post retrievePostById(Long postId) throws PostNotFoundException;

    public List<Post> retrievePostsByThreadId(Long threadId);

    public Post retrievePostByName(String postName) throws PostNotFoundException;

    public Long createPost(Post newPost, Long threadId, Long userId) throws PostNotFoundException, PostExistsException, ThreadNotFoundException, UserNotFoundException;

    public int retrievePostCount(Long threadId) throws ThreadNotFoundException;

    public ForumThread retrieveThreadById(Long threadId) throws ThreadNotFoundException;

    public void deletePost(Long postId, Long threadId) throws ThreadNotFoundException, NoResultException;
    
}
