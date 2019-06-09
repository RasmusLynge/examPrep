package test.utils;



import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Set;
import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class EmbeddedTomcat {

  private final static String TMP_DIR = System.getProperty("java.io.tmpdir")+"/EmbeddedTomcat";
  
  Tomcat tomcat;

  public void start(int port,String appContext) throws ServletException,
          MalformedURLException,
          LifecycleException {
    String webAppLocation = "src/main/webapp/";
    tomcat = new Tomcat();
    tomcat.setBaseDir(TMP_DIR);
    tomcat.setPort(port);

    // Define a web application context.
    Context context = tomcat.addWebapp(appContext, new File(webAppLocation).getAbsolutePath());
    ((StandardJarScanner) context.getJarScanner()).setScanManifest(false);
    
    // Add servlet that will register Jersey REST resources
    Tomcat.addServlet(context, "jersey-container-servlet", resourceConfig());
    context.addServletMappingDecoded("/api/*", "jersey-container-servlet");
    tomcat.start();
    //Don't comment the line below in unless you know what you do. It will block here, waiting for the server to stop. 
    //This can be usefull for testing the embedded server from a browser or postman
    //tomcat.getServer().await();
  }

  private ServletContainer resourceConfig() {
    //Load the Wizard-generated rest.ApplicationConfig - class
    Set<Class<?>> resources = new rest.ApplicationConfig().getClasses();
    System.out.println("Loaded Classes Count: "+resources.size());
    return new ServletContainer(new ResourceConfig(resources));
  }

  public void stop() {
    try {
      tomcat.stop();
      tomcat.destroy();
      //Delete our embedded Tomcat's temp
      FileUtils.deleteDirectory(new File(TMP_DIR));
    }
     catch (IOException | LifecycleException ex) {
       throw new RuntimeException(ex);
    }
  }
}