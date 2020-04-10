package com.company.forms.controller;

import java.text.MessageFormat;

import com.company.forms.bean.Request;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FormController {

        @GetMapping("/validateRequestForm")
        public String sendForm(Request request) {

            return "requestValidationForm";
        }

        @PostMapping("/executeTask")
        public RedirectView processForm(Request request) {
            String executeTaskOnBonitaURL = MessageFormat.format("{0}&valid={1}",request.getSubmitURL(), request.isValid());
            return new RedirectView(executeTaskOnBonitaURL);
        }
}
