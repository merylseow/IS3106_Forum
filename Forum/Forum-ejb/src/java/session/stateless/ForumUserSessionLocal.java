package session.stateless;

import entity.ForumUser;
import java.util.List;
import javax.ejb.Local;
import util.exception.UpdateUserException;
import util.exception.UserExistsException;
import util.exception.UserNotFoundException;

@Local
public interface ForumUserSessionLocal {

    public void createUser(ForumUser user) throws UserExistsException;
    
    public List<ForumUser> retrieveAllUsers();

    public ForumUser retrieveUserByUserId(Long userId) throws UserNotFoundException;

    public ForumUser retrieveUserByUsername(String username) throws UserNotFoundException;

    public boolean userLogin(String username, String password);

    public void updateUser(ForumUser user) throws UpdateUserException, UserNotFoundException;

    public void deleteUser(Long userId) throws UserNotFoundException;

    public boolean isExistingUsername(String username);

    public void blockUser(Long userId) throws UpdateUserException, UserNotFoundException;

    public void unblockUser(Long userId) throws UpdateUserException, UserNotFoundException;
}
