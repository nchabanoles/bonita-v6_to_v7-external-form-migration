package com.bonitasoft.v6form.migration.experimental.filter;

import java.io.IOException;
import java.text.MessageFormat;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.data.DataInstance;
import org.bonitasoft.engine.bpm.data.DataNotFoundException;
import org.bonitasoft.engine.session.APISession;


public class URLRewriter extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String submitURL = getSubmitURL(req);
        String parameters = buildParameters(req);
        String targetURL = MessageFormat.format("http://127.0.0.3:8888/validateRequestForm?{0}&submitURL={1}",parameters, submitURL);

        resp.sendRedirect(targetURL);
    }

    /*
    * This is the specific part for each external form. Because the list of query parameter is different for each of them.
    * */
    private String buildParameters(HttpServletRequest req) throws ServletException {

        ProcessAPI processAPI = new APIClient((APISession) req.getSession().getAttribute("apiSession")).getProcessAPI();
        long taskId = getTaskId(req);
        try {
            DataInstance description = processAPI.getActivityDataInstance("requestDescription",taskId);
            return MessageFormat.format("id={0}&description={1}",Long.toString(taskId), description.getValue());
        } catch (DataNotFoundException e) {
            throw new ServletException("Unable to build external form URL: missing variable in process instance.",e);
        }
    }

    private String getSubmitURL(HttpServletRequest req) {
        long id = getTaskId(req);
        return req.getRequestURL().toString() + "Submitter/?id=" + id;
    }

    private long getTaskId(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id").trim());
    }
}
