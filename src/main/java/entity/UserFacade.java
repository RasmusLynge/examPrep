package entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import exceptions.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import utils.PuSelector;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {
  
    private static EntityManagerFactory emf;
    private static UserFacade instance;
    
    private UserFacade(){}
    
    public static UserFacade getInstance(EntityManagerFactory factory){
        if(instance == null){
          emf = factory;
          instance = new UserFacade();
        }
        return instance;
    }
    
    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }
    
    public List<Wish> getUserWish(String username) {
        List<Wish> wishes = new ArrayList();
        return wishes;
    }

}
