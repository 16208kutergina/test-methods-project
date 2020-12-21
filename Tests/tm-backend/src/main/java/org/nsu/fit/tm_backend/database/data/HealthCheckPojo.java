package org.nsu.fit.tm_backend.database.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HealthCheckPojo {
    public HealthCheckPojo() {
        status = "OK";
    }

    @JsonProperty("status")
    public String status;
}
