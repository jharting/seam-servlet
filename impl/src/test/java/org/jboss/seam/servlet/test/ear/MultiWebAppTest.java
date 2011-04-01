package org.jboss.seam.servlet.test.ear;

import static org.junit.Assert.assertEquals;

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

/**
 * Test multiple web applications deployed within an EAR module.
 * @author <a href="http://community.jboss.org/people/jharting">Jozef Hartinger</a>
 *
 */
@RunWith(Arquillian.class)
public class MultiWebAppTest extends SingleWebAppTest {

    @Deployment(testable = false)
    public static EnterpriseArchive createDeployment() {
        Collection<GenericArchive> solder = DependencyResolvers.use(MavenDependencyResolver.class).loadReposFromPom("pom.xml")
                .artifact("org.jboss.seam.solder:seam-solder").exclusion("*").resolveAs(GenericArchive.class);
        JavaArchive servlet = createSeamServlet();
        WebArchive webApp1 = createWebAppTemplate("webapp1.war", "org/jboss/seam/servlet/test/ear/webapp1.xml")
                .addAsLibraries(solder.toArray(new Archive<?>[0])).addAsLibrary(servlet);
        WebArchive webApp2 = createWebAppTemplate("webapp2.war", "org/jboss/seam/servlet/test/ear/webapp2.xml")
                .addAsLibraries(solder.toArray(new Archive<?>[0])).addAsLibrary(servlet);
        EnterpriseArchive ear = createEarTemplate("test.ear", "org/jboss/seam/servlet/test/ear/multi-application.xml")
                .addAsModule(webApp1).addAsModule(webApp2);
        return ear;
    }

    @Test
    @Override
    public void testCorrectServletContextInjected() throws Exception {
        super.testCorrectServletContextInjected();
        assertEquals("/webapp2", getRepresentation("http://localhost:8080/webapp2/servletContextPath"));
    }

    @Test
    @Override
    public void testServletContextParameter() throws Exception {
        super.testServletContextParameter();
        assertEquals("bar2", getRepresentation("http://localhost:8080/webapp2/servletContextParameter"));
    }
    
}
