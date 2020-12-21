package org.nsu.fit.tm_backend.rest;

import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.util.logging.LogManager;

public class CustomApplication extends ResourceConfig {
    public CustomApplication() {
        try {
            LogManager.getLogManager().readConfiguration(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("./logging.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }

        packages("org.nsu.fit.tm_backend.rest");

        register(AuthenticationFilter.class);
        register(AuthorizationFilter.class);
        register(CORSFilter.class);

        register(GsonMessageBodyHandler.class);
    }
}
