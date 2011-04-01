package org.jboss.seam.servlet.test.ear;

import java.io.IOException;
import java.io.PrintWriter;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResultServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Inject
    private ServletContext servletContext;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        
        resp.setContentType("text/plain");
        String uri = req.getRequestURI();
        PrintWriter writer = resp.getWriter();
        
        if (uri.endsWith("servletContextPath"))
        {
            writer.write(servletContext.getContextPath());
        }
        else if (uri.endsWith("servletContextParameter"))
        {
            writer.write(servletContext.getInitParameter("foo"));
        }
        else
        {
            writer.write("Unrecognized request");
            resp.setStatus(400);
        }
    }

}
