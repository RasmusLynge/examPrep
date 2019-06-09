package testutils;

import entity.Role;
import entity.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;


public class TestUtils {
  
  public static void setupTestUsers(EntityManagerFactory emf) {
    EntityManager em = emf.createEntityManager();
    try {
      //System.out.println("XXXXXXXXXXXXXXXX  Creating Test users for TESTING XXXXXXXXXXXXXXXXXXXXXX");
      em.getTransaction().begin();
      //Delete existing users and roles to get a "fresh" database
      em.createQuery("delete from User").executeUpdate();
      em.createQuery("delete from Role").executeUpdate();
     
      Role userRole = new Role("user");
      Role adminRole = new Role("admin");
      User user = new User("user", "test");
      user.addRole(userRole);
      User admin = new User("admin", "test");
      admin.addRole(adminRole);
      User both = new User("user_admin", "test");
      both.addRole(userRole);
      both.addRole(adminRole);
      em.persist(userRole);
      em.persist(adminRole);
      em.persist(user);
      em.persist(admin);
      em.persist(both);
      System.out.println("Saved test data to database");
      em.getTransaction().commit();
    } finally {
      em.close();
    }
  }
  
}
