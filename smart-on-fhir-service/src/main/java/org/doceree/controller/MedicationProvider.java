package org.doceree.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.session.ClientSessionHolder;
import org.hspconsortium.client.session.Session;
import org.hspconsortium.client.session.authorizationcode.AuthorizationCodeSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@RestController
@Profile("r4")
public class MedicationProvider {

    @Inject
    private AuthorizationCodeSessionFactory<ClientSecretCredentials> ehrSessionFactory;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Autowired
    private ClientSessionHolder clientSessionHolder;

    @RequestMapping(method = RequestMethod.GET, path = "medicationRequest")
    public ResponseEntity<?> getMedicationDetail(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam String sessionId){
        Session ehrSession = clientSessionHolder.getSession(sessionId);
        if(ehrSession == null){
            return ResponseEntity.badRequest().build();
        }
        Collection<MedicationRequest> medicationRequest = ehrSession.getContextR4().getPatientContext().find(MedicationRequest.class);
        String jsonMedicationRequestDetail = gson.toJson(medicationRequest);
        return ResponseEntity.ok(jsonMedicationRequestDetail);
    }

}
