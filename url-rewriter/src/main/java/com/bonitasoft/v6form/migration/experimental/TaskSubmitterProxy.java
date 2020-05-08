package com.bonitasoft.v6form.migration.experimental;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bonitasoft.engine.api.APIClient;
import org.bonitasoft.engine.api.ProcessAPI;
import org.bonitasoft.engine.bpm.contract.ContractDefinition;
import org.bonitasoft.engine.bpm.contract.ContractViolationException;
import org.bonitasoft.engine.bpm.contract.InputDefinition;
import org.bonitasoft.engine.bpm.contract.Type;
import org.bonitasoft.engine.bpm.flownode.FlowNodeExecutionException;
import org.bonitasoft.engine.bpm.flownode.UserTaskNotFoundException;
import org.bonitasoft.engine.session.APISession;

public class TaskSubmitterProxy extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            ProcessAPI processAPI = new APIClient((APISession) req.getSession().getAttribute("apiSession")).getProcessAPI();
            long taskId = getTaskId(req);
            ContractDefinition contract = processAPI.getUserTaskContract(taskId);
            Map<String, Serializable> inputs = getInputs(req, contract);
            processAPI.executeUserTask(taskId,inputs);
        } catch (UserTaskNotFoundException | ContractViolationException | FlowNodeExecutionException e) {
            throw new ServletException("Unable to execute task from external URL Form.", e);
        }
        notifyPortalTaskSubmitted(resp);
    }

    private void notifyPortalTaskSubmitted(HttpServletResponse resp) throws ServletException {
        PrintWriter out;
        try {
            out = resp.getWriter();
            out.println("<html><body><script>");
            out.println("parent.postMessage({\"action\":\"Submit task\", \"message\": \"success\"});");
            out.println("</script></body></html>");
        } catch (IOException e) {
            throw new ServletException("Task executed, but unable to refresh Bonita Portal by sending message to parent iFrame.", e);
        }
    }

    private Map<String, Serializable> getInputs(HttpServletRequest req, ContractDefinition contract) {
        HashMap<String, Serializable> parameters = new HashMap<>();

        contract.getInputs().stream().forEach(inputDefinition -> {
            parameters.put(inputDefinition.getName(),extractParameterValue(req, inputDefinition));
        });
        return parameters;
    }

    private Serializable extractParameterValue(HttpServletRequest req, InputDefinition inputDefinition) {
        String parameterValue = req.getParameter(inputDefinition.getName());
        if(parameterValue==null) {
            parameterValue = req.getParameter(toParameterName(inputDefinition.getName()));
        }

        return cast(inputDefinition.getType(), parameterValue);

    }

    private String toParameterName(String name) {
        String suffix = "Input";
        if(name.endsWith(suffix)){
            return name.substring(0,name.length()-suffix.length());
        }
        return name;
    }

    private Serializable cast(Type type, String parameter) {
        switch (type) {
            case LONG:
                return Long.parseLong(parameter);
            case BOOLEAN:
                return Boolean.valueOf(parameter);
            case INTEGER:
                return Integer.parseInt(parameter);
            default:
                return parameter;
        }
    }

    private long getTaskId(HttpServletRequest req) {
        return Long.parseLong(req.getParameter("id"));
    }
}
