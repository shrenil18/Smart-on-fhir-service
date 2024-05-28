package org.doceree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
        basePackages = {"org.doceree", "org.hspconsortium.client"})
/*@EnableWebMvc*/
public class SoFApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SoFApplication.class, args);
    }

}

