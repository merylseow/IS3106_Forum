package session.stateless;

import entity.ForumUser;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateUserException;
import util.exception.UserExistsException;
import util.exception.UserNotFoundException;
import util.exception.UsernameExistsException;

@Stateless
public class ForumUserSession implements ForumUserSessionLocal {

    @PersistenceContext
    private EntityManager em;
    
    public ForumUserSession() {
    }
    
    @Override
    public void createUser(ForumUser u) throws UserExistsException {
        List<String> userNames = new ArrayList<>();
        for (ForumUser usr : retrieveAllUsers()) {
            userNames.add(usr.getUsername());
        }
        if (u.getUsername().isEmpty() || userNames.contains(u.getUsername())) {
            throw new UserExistsException("Username is not valid. Pick another one!");
        } else {
            em.persist(u);
            em.flush();
        }
    }

    @Override
    public List<ForumUser> retrieveAllUsers() {
        Query query = em.createQuery("SELECT u FROM ForumUser u");
        return query.getResultList();
    }

    @Override
    public ForumUser retrieveUserByUserId(Long userId) throws UserNotFoundException {
        ForumUser user = em.find(ForumUser.class, userId);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User Id does not exist");
        }
    }

    @Override
    public ForumUser retrieveUserByUsername(String username) throws UserNotFoundException {
        Query query = em.createQuery("SELECT u FROM ForumUser u WHERE u.username = :inUsername");
        query.setParameter("inUsername", username);

        try {
            ForumUser user = (ForumUser) query.getSingleResult();
            return user;

        } catch (NonUniqueResultException ex) {
            throw new UserNotFoundException("Username " + username + " does not exist!");
        }
    }

    @Override
    public void updateUser(ForumUser user) throws UpdateUserException, UserNotFoundException {
        if (user != null && user.getId() != null) {
            ForumUser userToUpdate = retrieveUserByUserId(user.getId());

            if (userToUpdate.getUsername().equals(user.getUsername())) {
                //userToUpdate.setFirstName(user.getFirstName());
                //userToUpdate.setLastName(user.getLastName());
                //userToUpdate.setDob(user.getDob());
                em.merge(user);
            } else {
                throw new UpdateUserException("Username of customer record to be updated does not match the existing record");
            }
        } else {
            throw new UserNotFoundException("Customer ID not provided for customer to be updated");
        }
    }

    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        ForumUser userToRemove = retrieveUserByUserId(userId);
        //userToRemove.getFavourites().remove(customerToRemove);
        //userToRemove.getDeals().remove(customerToRemove);
        em.remove(userToRemove);
    }

//    @Override
//    public ForumUser userLogin(String username, String password) {
//        try {
//            Query q = em.createQuery("SELECT u from ForumUser u WHERE u.username = :inUsername AND u.password = :inPassword");
//            q.setParameter("inUsername", username);
//            q.setParameter("inPassword", password);
//            return (ForumUser) q.getSingleResult();
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return null;
//    }
    @Override
    public boolean userLogin(String username, String password) {
        try {
            Query q = em.createQuery("SELECT u from ForumUser u WHERE u.username = :inUsername AND u.password = :inPassword");
            q.setParameter("inUsername", username);
            q.setParameter("inPassword", password);
            return !q.getResultList().isEmpty();
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @Override
    public boolean isExistingUsername(String username) {
        try {
            ForumUser u = retrieveUserByUsername(username);
        } catch (UserNotFoundException ex) {
            return false;
        }
        return true;
    }

    @Override
    public void blockUser(Long userId) throws UpdateUserException, UserNotFoundException {
        ForumUser u = retrieveUserByUserId(userId);
        u.setUserStatus("BLOCKED");
        updateUser(u);
    }

    @Override
    public void unblockUser(Long userId) throws UpdateUserException, UserNotFoundException {
        ForumUser u = retrieveUserByUserId(userId);
        u.setUserStatus("ACTIVE");
        updateUser(u);
    }
}
