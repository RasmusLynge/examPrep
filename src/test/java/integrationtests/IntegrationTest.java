package integrationtests;

import org.junit.BeforeClass;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.*;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.MalformedURLException;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import static org.hamcrest.Matchers.*;
import org.junit.AfterClass;
import org.junit.Test;
import test.utils.EmbeddedTomcat;
import testutils.TestUtils;
import utils.PuSelector;

public class IntegrationTest {

  protected static int SERVER_PORT;
  protected static String APP_CONTEXT;
  protected static String SERVER_URL;

  public IntegrationTest() {
  }

  private static EmbeddedTomcat tomcat;

  @BeforeClass
  public static void setUpBeforeAll() throws ServletException, MalformedURLException, LifecycleException {
    System.out.println("INTEGRATION TEST");
    TestUtils.setupTestUsers(PuSelector.getEntityManagerFactory("pu_integration_test"));
    SERVER_PORT = 7777;
    APP_CONTEXT = "/jwtbackend";
    SERVER_URL = "http://localhost";

    //Setup and start Embedded Tomcat for testing
    tomcat = new EmbeddedTomcat();
    tomcat.start(SERVER_PORT, APP_CONTEXT);

    //Setup RestAssured
    RestAssured.baseURI = SERVER_URL;
    RestAssured.port = SERVER_PORT;
    RestAssured.basePath = APP_CONTEXT;
    RestAssured.defaultParser = Parser.JSON;
  }

  @AfterClass
  public static void tearDownAfterAll() {
    tomcat.stop();
  }

  //This is how we hold on to the token after login, similar to that a client must store the token somewhere
  private static String securityToken;

  //Utility method to login and set the returned securityToken
  private static void login(String role, String password) {
    String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
    securityToken = given()
            .contentType("application/json")
            .body(json)
            .when().post("/api/login")
            .then()
            .extract().path("token");
  }

  private void logOut() {
    securityToken = null;
  }

  @Test
  public void serverIsRunning() {
    System.out.println("Testing is server UP");
    given().when().get("/").then().statusCode(200);
  }

  @Test
  public void testRestNoAuthenticationRequired() {
    given()
            .contentType("application/json")
            .when()
            .get("/api/info").then()
            .statusCode(200)
            .body("msg", equalTo("Hello anonymous"));
  }

  @Test
  public void testRestForAdmin() {
    login("admin", "test");
    given()
            .contentType("application/json")
            .accept(ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/admin").then()
            .statusCode(200)
            .body("name", equalTo("admin"));
  }

  @Test
  public void testRestForUser() {
    login("user", "test");
    given()
            .contentType("application/json")
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/user").then()
            .statusCode(200)
            .body("name", equalTo("user"));
  }
  
  //@Test
  public void testRestForMultiRole1() {
    login("user_admin", "test");
    given()
            .contentType("application/json")
            .accept(ContentType.JSON)
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/admin").then()
            .statusCode(200)
            .body("name", equalTo("user_admin"));
  }

  //@Test
  public void testRestForMultiRole2() {
    login("user_admin", "test");
    given()
            .contentType("application/json")
            .header("x-access-token", securityToken)
            .when()
            .get("/api/info/user").then()
            .statusCode(200)
            .body("name", equalTo("user_admin"));
  }

  @Test
  public void userNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/info/user").then()
            .statusCode(403);
  }

  @Test
  public void adminNotAuthenticated() {
    logOut();
    given()
            .contentType("application/json")
            .when()
            .get("/api/info/user").then()
            .statusCode(403);
  }

}
