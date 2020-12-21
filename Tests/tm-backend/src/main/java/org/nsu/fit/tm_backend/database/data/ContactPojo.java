package org.nsu.fit.tm_backend.database.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContactPojo {
    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("login")
    public String login;

    /**
     * Лабораторная *: здесь следует обратить внимание на хранение и передачу пароля
     * в открытом виде, почему это плохо, как можно исправить.
     */
    @JsonProperty("password")
    public String password;

    @JsonProperty("balance")
    public int balance;
}
