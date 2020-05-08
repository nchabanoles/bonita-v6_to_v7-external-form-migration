package com.bonitasoft.v6form.migration.experimental.filter;

import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.session.APISession;


public class URLRewriter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        URL formURL = getFormURL(req);
        String submitURL = getSubmitURL(req);
        String redirectionURL = MessageFormat.format("{0}&{1}&submitURL={2}",formURL.toString(), req.getQueryString(), submitURL);

        resp.sendRedirect(redirectionURL);
    }

    private URL getFormURL(HttpServletRequest req) throws ServletException {
        ProcessAPI processAPI = new APIClient((APISession) req.getSession().getAttribute("apiSession")).getProcessAPI();
        long taskId = getTaskId(req);
        try {
            return (URL) processAPI.getActivityDataInstance("externalFormURL",taskId).getValue();

        } catch (DataNotFoundException e) {
            throw new ServletException("Unable to build external form URL: missing variable in process instance. " + e.getMessage());
        }
    }

    private String getSubmitURL(HttpServletRequest req) {
        long id = getTaskId(req);
        String currentServlet = req.getRequestURL().toString();
        if(req.getPathInfo()!=null) {
            currentServlet = currentServlet.substring(0,currentServlet.length() - req.getPathInfo().length());
        }
        return currentServlet + "Submitter/?id=" + id;
    }
    private long getTaskId(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id").trim());
    }
}
