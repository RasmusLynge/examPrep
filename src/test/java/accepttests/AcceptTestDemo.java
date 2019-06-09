package accepttests;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.MalformedURLException;
import javax.persistence.EntityManagerFactory;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AcceptTestDemo {
  private static final String APP_CONTEXT = "/jwtbackend";  //IMPORTANT--> this should reflect the value in META-INF/context.xml
  private static EntityManagerFactory emf;
  
  @Test
  public void serverIsRunning() {
    System.out.println("TESTING IF SERVER IS RUNNING");
    given().when().get().then().statusCode(200); 
  }

  @BeforeClass
  public static void setUpBeforeAll() throws LifecycleException, ServletException, MalformedURLException {
     RestAssured.baseURI = "https://deploy.mydemos.dk/";
    //RestAssured.port = 80;
    //RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
  }

  @AfterClass
  public static void tearDownAfterAll() throws LifecycleException {
   
  }
}


