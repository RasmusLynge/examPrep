package utils;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.eclipse.persistence.config.PersistenceUnitProperties;

// Returns an EntityManagerFactory using the provided PU_NAME)
// DO NOT CHANGE THIS FILE, UNLESS YOU REALLY KNOW WHAT YOU DO
public class PuSelector {

  private static String PU_NAME;
  private static EntityManagerFactory emf;
  public static final String FILE_EXTENSION = ".properties";

  public static Properties loadProperties(String propertyFile) {
    Properties props = new Properties();
    String propertyFileName = propertyFile + FILE_EXTENSION;
    try {
      props.load(PuSelector.class.getResourceAsStream("/META-INF/" + propertyFileName));
      for (Object key : props.keySet()) {
        props.setProperty((String) key, props.getProperty((String) key).trim());
      }
    } catch (Exception ex) {
      throw new RuntimeException("Could not load properies for :" + propertyFileName);
    }
    return props;
  }

  public static EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {

    //This ensures that only ONE factory will ever be used. If a test has set to a test-db, this will be used also for example from the login end-point
    if (emf != null) {
      System.out.println("Returned an EntityManagerFactory for  --> " + emf.getProperties().get("javax.persistence.jdbc.url"));
      return emf;
    }

    PU_NAME = persistenceUnitName;

    //You can override the given PU_NAME from maven like this for integration tests: mvn -DPU_NAME=pu-test-on-travis verify
    String puVal = System.getProperty("PU_NAME");
    if (puVal != null) {
      PU_NAME = puVal;
    }

    Properties props = null;
    /*
      If you set the following environment variables on your production server, the production persistence unit will use them.
      export SERVER="PRODUCTION"
      export USER="YOUR_DATABASE_USER"
      export PASSWORD="YOUR PASSWORD FOR THE PRODUCTION DB"
    
      MUST BE SET in this file on Digital Ocean (if file don't exist, create it):  /usr/share/tomcat8/bin/setenv.sh
     */
    
    System.out.println("SERVER -->"+System.getenv("SERVER"));
    boolean isDeployed = (System.getenv("SERVER") != null);
    if (isDeployed) {
      if (System.getenv("SERVER").equals("PRODUCTION")) {  //You could also setup a Test server where you set SERVER="TEST"
        PU_NAME = "pu_production";
        System.out.println("PU_NAME: "+PU_NAME);
        props = loadProperties(PU_NAME);
        String user = System.getenv("USER") != null ? System.getenv("USER") : "";
        String password = System.getenv("PASSWORD") != null ? System.getenv("PASSWORD") : "";
        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", password);
      }
    }
     //If NOT deployed
    if (props == null) {
      props = loadProperties(PU_NAME);
    }

    //Only reason to give persistence file another name is that it must NOT be git-ignored, which is what we usually do with persistence.xml
    props.setProperty(PersistenceUnitProperties.ECLIPSELINK_PERSISTENCE_XML,
            "META-INF/persistence-for-all.xml");

    emf = Persistence.createEntityManagerFactory("DO_NOT_RENAME_ME", props);
    return emf;
  }
}
