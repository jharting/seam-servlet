package org.jboss.seam.servlet.test.ear;

import java.util.Collection;

import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.GenericArchive;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.DependencyResolvers;
import org.jboss.shrinkwrap.resolver.api.maven.MavenDependencyResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.assertEquals;

/**
 * Test single web application deployed within an EAR module.
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 *
 */
@RunWith(Arquillian.class)
public class SingleWebAppTest extends AbstractClientTest {

    @Deployment(testable = false)
    public static EnterpriseArchive createDeployment() {
        Collection<GenericArchive> solder = DependencyResolvers.use(MavenDependencyResolver.class).loadReposFromPom("pom.xml")
                .artifact("org.jboss.seam.solder:seam-solder").exclusion("*").resolveAs(GenericArchive.class);
        JavaArchive servlet = createSeamServlet();
        WebArchive war = createWebAppTemplate("webapp1.war", "org/jboss/seam/servlet/test/ear/webapp1.xml")
                .addAsLibraries(solder.toArray(new Archive<?>[0])).addAsLibrary(servlet);
        EnterpriseArchive ear = createEarTemplate("test.ear", "org/jboss/seam/servlet/test/ear/single-application.xml")
                .addAsModule(war);
        return ear;
    }

    @Test
    public void testCorrectServletContextInjected() throws Exception {
        assertEquals("/webapp1", getRepresentation("http://localhost:8080/webapp1/servletContextPath"));
    }
    
    @Test
    public void testServletContextParameter() throws Exception {
        assertEquals("bar1", getRepresentation("http://localhost:8080/webapp1/servletContextParameter"));
    }
}
