package org.doceree.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.session.ClientSessionHolder;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.authorizationcode.AuthorizationCodeSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;

@Controller
@Profile("r4")
public class HomeController {

    @Inject
    private AuthorizationCodeSessionFactory<ClientSecretCredentials> ehrSessionFactory;
    Gson gson = new GsonBuilder().create();

    @Autowired
    private ClientSessionHolder clientSessionHolder;

    @RequestMapping(value = "/welcome", method = RequestMethod.GET)
    public String landingPage(HttpSession httpSession, Model model, HttpServletRequest request) throws IOException {
        Session ehrSession =(Session) httpSession.getAttribute(ehrSessionFactory.getSessionKey());
        request.getParameterNames();
        Patient patient = ehrSession.getContextR4().getPatientResource();
        if(ehrSession == null){
            model.addAttribute("Session not found");
        }
        return "home";
        //return "redirect:http://192.168.1.130:3000/";
    }

    @RequestMapping(method = RequestMethod.GET, path = "practitioner")
    public ResponseEntity<?> getPractitionerDetail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam String sessionId){
        Session ehrSession = clientSessionHolder.getSession(sessionId);
        if(ehrSession == null){
            return ResponseEntity.badRequest().build();
        }

        Collection<Practitioner> practitioners =(Collection<Practitioner>) ehrSession.getContextR4().getCurrentUserResource();
        String practitonerDetail =gson.toJson(practitioners);
        return ResponseEntity.ok(practitonerDetail);
    }

}
