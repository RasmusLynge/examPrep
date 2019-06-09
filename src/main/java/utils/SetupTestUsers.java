package utils;

import entity.Role;
import entity.User;
import entity.Wish;
import javax.persistence.EntityManager;

public class SetupTestUsers {

    public static void main(String[] args) {

        EntityManager em = PuSelector.getEntityManagerFactory("pu").createEntityManager();

        // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
        // CHANGE the three passwords below, before you uncomment and execute the code below
        //throw new UnsupportedOperationException("REMOVE THIS LINE, WHEN YOU HAVE READ WARNING");
        em.getTransaction().begin();
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User user = new User("user", "userpw");
        user.addRole(userRole);
        User admin = new User("admin", "adminpw");
        admin.addRole(adminRole);
        Wish wish = new Wish("23", user);
        em.persist(wish);
        Wish wish2 = new Wish("25", user);
        em.persist(wish2);
        em.persist(userRole);
        em.persist(adminRole);
        em.persist(user);
        em.persist(admin);
        em.getTransaction().commit();
        System.out.println("PW: " + user.getUserPass());
        System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
        System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
        System.out.println("Created TEST Users");

    }

}
