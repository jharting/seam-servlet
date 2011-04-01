package org.jboss.seam.servlet.test.ear;

import static org.junit.Assert.assertEquals;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.seam.servlet.ServletExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.impl.base.filter.ExcludeRegExpPaths;

public class AbstractClientTest {
    
    private HttpClient client = new HttpClient();

    protected String getRepresentation(String url) throws Exception {
        GetMethod get = new GetMethod(url);
        get.setRequestHeader("Accept", "text/plain");
        assertEquals(200, client.executeMethod(get));
        return get.getResponseBodyAsString();
    }
    
    public static JavaArchive createSeamServlet() {
        return ShrinkWrap
                .create(JavaArchive.class, "seam-servlet.jar")
                .addAsManifestResource("META-INF/beans.xml", "beans.xml")
                .addAsManifestResource("META-INF/web-fragment.xml", "web-fragment.xml")
                .addAsManifestResource("META-INF/services/javax.enterprise.inject.spi.Extension",
                        "services/javax.enterprise.inject.spi.Extension")
                .addAsManifestResource("META-INF/services/org.jboss.weld.extensions.beanManager.BeanManagerProvider",
                        "services/org.jboss.weld.extensions.beanManager.BeanManagerProvider")
                .addPackages(true, new ExcludeRegExpPaths("^.*?test.*?$"), ServletExtension.class.getPackage());
    }

    public static WebArchive createWebAppTemplate(String appName, String webXml, Archive<?>... libs) {
        return ShrinkWrap.create(WebArchive.class, appName).addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addClass(ResultServlet.class).setWebXML(webXml).addAsLibraries(libs);
    }

    public static EnterpriseArchive createEarTemplate(String name, String applicationXml, WebArchive... webApps) {
        EnterpriseArchive ear = ShrinkWrap.create(EnterpriseArchive.class, name).setApplicationXML(applicationXml);
        for (WebArchive webApp : webApps) {
            ear.addAsModule(webApp);
        }
        return ear;
    }
}
