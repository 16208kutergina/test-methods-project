package org.nsu.fit.services.rest.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class
HealthCheckPojo {
    public HealthCheckPojo() {
        status = "OK";
    }

    @JsonProperty("status")
    public String status;
}
