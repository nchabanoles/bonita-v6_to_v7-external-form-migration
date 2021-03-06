package com.company.forms.bean;

public class Request {

    private String id;
    private String description;
    private boolean valid;

    // Used to forward data to Bonita after the form is submitted
    private String submitURL;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


    public String getSubmitURL() {
        return submitURL;
    }

    public void setSubmitURL(String submitURL) {
        this.submitURL = submitURL;
    }
}