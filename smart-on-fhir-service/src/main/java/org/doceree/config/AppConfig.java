package org.doceree.config;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IRestfulClientFactory;
import org.hspconsortium.client.auth.StateProvider;
import org.hspconsortium.client.auth.access.AccessTokenProvider;
import org.hspconsortium.client.auth.access.JsonAccessTokenProvider;
import org.hspconsortium.client.auth.authorizationcode.AuthorizationCodeRequestBuilder;
import org.hspconsortium.client.auth.credentials.ClientSecretCredentials;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.hspconsortium.client.session.ApacheHttpClientFactory;
import org.hspconsortium.client.session.FhirSessionContextHolder;
import org.hspconsortium.client.session.SessionKeyRegistry;
import org.hspconsortium.client.session.authorizationcode.AuthorizationCodeSessionFactory;
import org.hspconsortium.client.session.impl.SimpleFhirSessionContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;
import java.util.UUID;

@Configuration
public class AppConfig {

    @Value("${home.clientId}")
    private String clientId;

    @Value("${home.scope}")
    private String scope;

    @Value("${home.redirectUri}")
    private String redirectUri;

    @Value("${home.clientSecret}")
    private String clientSecret;

    @Value("${home.appEntryPoint}")
    private String appEntryPoint;

    @Value("${home.httpConnectionTimeoutMilliSeconds:}")
    private String httpConnectionTimeoutMilliSeconds;

    @Value("${home.httpReadTimeoutMilliSeconds:}")
    private String httpReadTimeoutMilliSeconds;


    @Bean
    public String clientId() {
        return clientId;
    }

    @Bean
    public String scope() {
        return scope;
    }

    @Bean
    public String redirectUri() {
        return redirectUri;
    }

    @Bean
    public String clientSecret() {
        return clientSecret;
    }

    @Bean
    public String appEntryPoint() {
        return appEntryPoint;
    }


    @Bean
    public Integer httpConnectionTimeOut() {
        return Integer.parseInt(
                httpConnectionTimeoutMilliSeconds != null && httpConnectionTimeoutMilliSeconds.length() > 0
                        ? httpConnectionTimeoutMilliSeconds
                        : IRestfulClientFactory.DEFAULT_CONNECT_TIMEOUT + "");
    }

    @Bean
    public Integer httpReadTimeOut() {
        return Integer.parseInt(
                httpReadTimeoutMilliSeconds != null && httpReadTimeoutMilliSeconds.length() > 0
                        ? httpReadTimeoutMilliSeconds
                        : IRestfulClientFactory.DEFAULT_CONNECTION_REQUEST_TIMEOUT + "");
    }

    @Bean
    public String proxyPassword() {
        return System.getProperty("http.proxyPassword", System.getProperty("https.proxyPassword"));
    }

    @Bean
    public String proxyUser() {
        return System.getProperty("http.proxyUser", System.getProperty("https.proxyUser"));
    }

    @Bean
    public Integer proxyPort() {
        return Integer.parseInt(System.getProperty("http.proxyPort", System.getProperty("https.proxyPort", "8080")));
    }

    @Bean
    public String proxyHost() {
        //To Use With Proxy
        //-Dhttp.proxyHost=proxy.host.com -Dhttp.proxyPort=8080  -Dhttp.proxyUser=username -Dhttp.proxyPassword=password
        return System.getProperty("http.proxyHost", System.getProperty("https.proxyHost"));
    }

    @Bean
    public StateProvider stateProvider() {
        return new StateProvider.DefaultStateProvider();
    }

    @Bean
    public FhirSessionContextHolder fhirSessionContextHolder() {
        return new SimpleFhirSessionContextHolder();
    }

    @Bean
    @Inject
    public ApacheHttpClientFactory apacheHttpClientFactory() {
        return new ApacheHttpClientFactory(proxyHost(), proxyPort(), proxyUser(), proxyPassword(),
                httpConnectionTimeOut(), httpReadTimeOut());
    }

    @Bean
    public AccessTokenProvider accessTokenProvider(ApacheHttpClientFactory apacheHttpClientFactory) {
        return new JsonAccessTokenProvider(apacheHttpClientFactory);
    }

    @Bean
    @Inject
    public AuthorizationCodeRequestBuilder authorizationCodeRequestBuilder(FhirEndpointsProvider fhirEndpointsProvider,
                                                                           StateProvider stateProvider) {
        return new AuthorizationCodeRequestBuilder(fhirEndpointsProvider, stateProvider);
    }

    @Bean
    @Inject
    public ClientSecretCredentials clientSecretCredentials(String clientSecret) {
        return new ClientSecretCredentials(clientSecret);
    }

    @Bean
    public SessionKeyRegistry sessionKeyRegistry() {
        return new SessionKeyRegistry();
    }

    @Bean
    @Inject
    public AuthorizationCodeSessionFactory<ClientSecretCredentials>
    authorizationCodeSessionFactory(FhirContext fhirContext, SessionKeyRegistry sessionKeyRegistry,
                                    FhirSessionContextHolder fhirSessionContextHolder,
                                    AccessTokenProvider patientAccessTokenProvider,
                                    String clientId, ClientSecretCredentials clientSecretCredentials, String redirectUri) {
        return new AuthorizationCodeSessionFactory<>(fhirContext, sessionKeyRegistry, UUID.randomUUID().toString(), fhirSessionContextHolder,
                patientAccessTokenProvider, clientId, clientSecretCredentials, redirectUri);
    }

}
