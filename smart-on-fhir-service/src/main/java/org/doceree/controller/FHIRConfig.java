package org.doceree.controller;

import ca.uhn.fhir.context.FhirContext;
import org.doceree.config.AppConfig;
import org.hspconsortium.client.controller.FHIREndpointsProviderR4;
import org.hspconsortium.client.controller.FhirEndpointsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("r4")
public class FHIRConfig {
    @Autowired
    private AppConfig appConfig;

    @Bean
    public FhirEndpointsProvider fhirEndpointsProvider(FhirContext fhirContext){
        return new FHIREndpointsProviderR4(fhirContext);
    }

    @Bean
    public FhirContext fhirContext() {
        FhirContext hapiFhirContext = FhirContext.forR4();
        // Set how long to try and establish the initial TCP connection (in ms)
        hapiFhirContext.getRestfulClientFactory().setConnectTimeout(appConfig.httpConnectionTimeOut());

        // Set how long to block for individual read/write operations (in ms)
        hapiFhirContext.getRestfulClientFactory().setSocketTimeout(appConfig.httpReadTimeOut());

        if (appConfig.proxyHost() != null) {
            hapiFhirContext.getRestfulClientFactory().setProxy(appConfig.proxyHost(), appConfig.proxyPort());

            hapiFhirContext.getRestfulClientFactory().setProxyCredentials(appConfig.proxyUser()
                    , appConfig.proxyPassword());
        }
        return hapiFhirContext;
    }
}
